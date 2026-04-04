package com.zoksh.core.database

import androidx.room.Database
import com.zoksh.core.database.dao.RunDao
import com.zoksh.core.database.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1,
)
abstract class RunDatabase {
    abstract val dao: RunDao
}