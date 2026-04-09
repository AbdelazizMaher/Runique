package com.zoksh.core.data.run

import com.zoksh.core.domain.run.LocalRunDataSource
import com.zoksh.core.domain.run.RemoteRunDataSource
import com.zoksh.core.domain.run.Run
import com.zoksh.core.domain.run.RunRepository
import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.EmptyResult
import com.zoksh.core.domain.util.Result
import com.zoksh.core.domain.util.asEmptyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localDataSource: LocalRunDataSource,
    private val remoteDataSource: RemoteRunDataSource,
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

        applicationScope.async {
            remoteDataSource.deleteRun(id)
        }.await()
    }
}