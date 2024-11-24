package com.pakenanya.mindsync.data.local.repository

import com.pakenanya.mindsync.data.local.room.UserDao
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences
import com.pakenanya.mindsync.data.remote.retrofit.AuthApiService

class AuthRepository(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val preferencesManager: MindSyncAppPreferences
) {

}