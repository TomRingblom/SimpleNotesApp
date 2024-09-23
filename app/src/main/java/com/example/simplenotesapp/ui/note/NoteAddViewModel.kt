package com.example.simplenotesapp.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplenotesapp.data.NoteRepository

class NoteAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

}