package com.zoksh.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zoksh.core.database.dao.RunDao
import com.zoksh.core.database.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1,
)
abstract class RunDatabase : RoomDatabase() {
    abstract val dao: RunDao
}
