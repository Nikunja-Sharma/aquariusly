package com.nikunja.aquariusly.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikunja.aquariusly.data.local.dao.ItemDao
import com.nikunja.aquariusly.data.local.entity.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
