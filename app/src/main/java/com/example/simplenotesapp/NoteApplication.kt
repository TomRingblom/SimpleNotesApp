package com.example.simplenotesapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.simplenotesapp.data.AppContainer
import com.example.simplenotesapp.data.AppDataContainer
import com.example.simplenotesapp.data.UserPreferencesRepository

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class NoteApplication : Application() {
    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository


    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        container = AppDataContainer(this)
    }

}