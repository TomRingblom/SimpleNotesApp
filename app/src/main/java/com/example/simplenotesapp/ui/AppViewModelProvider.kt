package com.example.simplenotesapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simplenotesapp.NoteApplication
import com.example.simplenotesapp.NotesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NotesViewModel(inventoryApplication().container.noteRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): NoteApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)