package com.example.simplenotesapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simplenotesapp.NoteApplication
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.ui.note.NoteEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[APPLICATION_KEY] as NoteApplication)
            NotesViewModel(inventoryApplication().container.noteRepository, application.userPreferencesRepository)
        }
        initializer {
            NoteEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.noteRepository
            )
        }
    }
}

fun CreationExtras.inventoryApplication(): NoteApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)