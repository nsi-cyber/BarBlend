package com.nsicyber.barblend.data.datastore


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class DataStoreManager @Inject constructor(private val context: Context) {


    suspend fun compare(newData:String): Flow<Boolean> {
        val dataStoreKey = stringPreferencesKey("key")
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