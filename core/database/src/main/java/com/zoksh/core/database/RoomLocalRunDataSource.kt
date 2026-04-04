package com.zoksh.core.database

import com.zoksh.core.database.dao.RunDao
import com.zoksh.core.domain.run.LocalRunDataSource
import com.zoksh.core.domain.run.Run
import com.zoksh.core.domain.run.RunId
import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

class RoomLocalRunDataSource(
    private val runDao: RunDao,
) : LocalRunDataSource {
    override fun getRuns(): Flow<List<Run>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRun(id: RunId) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRuns() {
        TODO("Not yet implemented")
    }
}