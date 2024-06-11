package com.nsicyber.barblend.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nsicyber.barblend.common.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = Constants.DataStore.USER_PREFERENCES_NAME)

class DataStoreManager
    @Inject
    constructor(private val context: Context) {
        fun compare(newData: String): Flow<Boolean> {
            val dataStoreKey = stringPreferencesKey(Constants.DataStore.USER_PREFERENCES_KEY)
            return context.dataStore.data.map { preferences ->
                val currentData = preferences[dataStoreKey]
                if (currentData != newData) {
                    context.dataStore.edit { prefs ->
                        prefs[dataStoreKey] = newData
                    }
                    true
                } else {
                    false
                }
            }
        }
    }
