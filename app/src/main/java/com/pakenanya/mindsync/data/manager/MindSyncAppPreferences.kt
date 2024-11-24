package com.pakenanya.mindsync.data.manager

import javax.inject.Inject
import android.content.Context
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MindSyncAppPreferences")

class MindSyncAppPreferences @Inject constructor(
    private val context: Context
) {
    val shouldShowOnboarding: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING] != true
        }

    suspend fun completeOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING] = true
        }
    }

    fun getToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            val token = preferences[TOKEN] ?: ""
            token
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun saveStreakInfo(lastEntryDate: String, streakCount: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAST_ENTRY_DATE] = lastEntryDate
            preferences[STREAK_COUNT] = streakCount.toString()
        }
    }

    fun getStreakInfo(): Flow<Pair<String, Int>> {
        return context.dataStore.data.map { preferences ->
            val lastEntryDate = preferences[LAST_ENTRY_DATE] ?: ""
            val streakCount = preferences[STREAK_COUNT]?.toInt() ?: 0
            Pair(lastEntryDate, streakCount)
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    fun getNotificationsState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_ENABLED] ?: false
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val STREAK_COUNT = stringPreferencesKey("streak_count")
        private val LAST_ENTRY_DATE = stringPreferencesKey("last_entry_date")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    }
}