package com.example.simplenotesapp

import android.app.Application
import com.example.simplenotesapp.data.AppContainer
import com.example.simplenotesapp.data.AppDataContainer

class NoteApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}