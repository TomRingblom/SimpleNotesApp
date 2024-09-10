package com.example.simplenotesapp.data

import android.content.Context

interface AppContainer {
    val noteRepository: NoteRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val noteRepository: NoteRepository by lazy {
        NoteOfflineRepository(NoteDatabase.getDatabase(context).noteDao())
    }
}