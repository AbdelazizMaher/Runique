package com.zoksh.core.data.run

import com.zoksh.core.database.dao.RunPendingSyncDao
import com.zoksh.core.database.mappers.toRun
import com.zoksh.core.domain.SessionStorage
import com.zoksh.core.domain.run.LocalRunDataSource
import com.zoksh.core.domain.run.RemoteRunDataSource
import com.zoksh.core.domain.run.Run
import com.zoksh.core.domain.run.RunRepository
import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.EmptyResult
import com.zoksh.core.domain.util.Result
import com.zoksh.core.domain.util.asEmptyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localDataSource: LocalRunDataSource,
    private val remoteDataSource: RemoteRunDataSource,
    private val sessionStorage: SessionStorage,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val applicationScope: CoroutineScope
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteDataSource.getRuns()) {
            is Result.Error -> {
                result.asEmptyResult()
            }

            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertRuns(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(
        run: Run,
        mapPicture: ByteArray
    ): EmptyResult<DataError> {
        val localResult = localDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }

        val runWithId = run.copy(id = localResult.data)
        return when (val remoteResult = remoteDataSource.postRun(runWithId, mapPicture)) {
            is Result.Error -> {
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertRun(remoteResult.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: String) {
        localDataSource.deleteRun(id)

        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        applicationScope.async {
            remoteDataSource.deleteRun(id)
        }.await()
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }
            val deletedRuns = async {
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createdJobs = createdRuns.await().map {
                launch {
                    val runEntity = it.run.toRun()
                    when (remoteDataSource.postRun(runEntity, it.mapPicture)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }
            val deletedJobs = deletedRuns.await().map {
                launch {
                    when (remoteDataSource.deleteRun(it.runId)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }

            createdJobs.joinAll()
            deletedJobs.joinAll()
        }
    }
}