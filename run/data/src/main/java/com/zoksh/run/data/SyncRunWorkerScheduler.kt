package com.zoksh.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.zoksh.core.database.dao.RunPendingSyncDao
import com.zoksh.core.database.entity.DeletedRunSyncEntity
import com.zoksh.core.database.entity.RunPendingSyncEntity
import com.zoksh.core.database.mappers.toRunEntity
import com.zoksh.core.domain.SessionStorage
import com.zoksh.core.domain.run.Run
import com.zoksh.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
) : SyncRunScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncRunScheduler.SyncType) {
        when (type) {
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(
                type.run,
                type.mapPictureBytes
            )

            is SyncRunScheduler.SyncType.DeleteRun -> scheduleDeleteRunWorker(type.runId)
            is SyncRunScheduler.SyncType.FetchRuns -> scheduleFetchRunWorker(type.interval)
        }
    }

    override suspend fun cancelAllSync() {
        workManager.cancelAllWork().await()
    }

    private suspend fun scheduleFetchRunWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }

        if (isSyncScheduled) return

        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.MILLISECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag("sync_work").build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureBytes: ByteArray) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingRun = RunPendingSyncEntity(
            run = run.toRunEntity(),
            mapPictureBytes = mapPictureBytes,
            userId = userId
        )
        pendingSyncDao.upsertRunPendingSyncEntity(pendingRun)

        val workRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .addTag("create_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            ).setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID, pendingRun.runId)
                    .build()
            ).build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteRunWorker(runId: String) {
        val userId = sessionStorage.get()?.userId ?: return
        val deletedRunSyncEntity = DeletedRunSyncEntity(
            runId = runId,
            userId = userId
        )
        pendingSyncDao.upsertDeletedRunSyncEntity(deletedRunSyncEntity)

        val workRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .addTag("delete_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            ).setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID, deletedRunSyncEntity.runId)
                    .build()
            ).build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }
}