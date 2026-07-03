package com.sportz.base.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLocalStorageManager @Inject constructor(
    val gson: Gson,
    val dataStore: DataStore<Preferences>,
) {
    private val userTokenKey = stringPreferencesKey("user_token")

    suspend fun setUserToken(token: String) {
        dataStore.edit { prefs -> prefs[userTokenKey] = token }
    }

    fun getUserToken(): Flow<String?> {
        return dataStore.data.map { prefs -> prefs[userTokenKey] }
    }

    suspend fun removeUserToken() {
        dataStore.edit { prefs -> prefs.remove(userTokenKey) }
    }
}