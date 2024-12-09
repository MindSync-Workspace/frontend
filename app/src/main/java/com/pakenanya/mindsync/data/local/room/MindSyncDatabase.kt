package com.pakenanya.mindsync.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pakenanya.mindsync.data.remote.response.ChatsData
import com.pakenanya.mindsync.data.remote.response.DocumentsData
import com.pakenanya.mindsync.data.remote.response.MembershipData
import com.pakenanya.mindsync.data.remote.response.NotesData
import com.pakenanya.mindsync.data.remote.response.OrganizationData
import com.pakenanya.mindsync.data.remote.response.UserData

@Database(
    entities = [
        UserData::class,
        OrganizationData::class,
        MembershipData::class,
        NotesData::class,
        DocumentsData::class,
        ChatsData::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MindSyncDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun organizationsDao(): OrganizationsDao
    abstract fun membershipsDao(): MembershipsDao
    abstract fun notesDao(): NotesDao
    abstract fun documentsDao(): DocumentsDao
    abstract fun chatsDao(): ChatsDao
}