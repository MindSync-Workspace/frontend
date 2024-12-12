package com.pakenanya.mindsync.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.pakenanya.mindsync.data.local.room.UserDao
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences

class AuthRepository(
    private val userDao: UserDao,
    private val preferencesManager: MindSyncAppPreferences
) {
    fun getToken(): LiveData<String> {
        return preferencesManager.getToken().asLiveData()
    }
}