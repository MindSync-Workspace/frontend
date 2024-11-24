package com.pakenanya.mindsync.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pakenanya.mindsync.data.remote.response.UserData

@Database(entities = [UserData::class], version = 2, exportSchema = false)
abstract class MindSyncDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
}