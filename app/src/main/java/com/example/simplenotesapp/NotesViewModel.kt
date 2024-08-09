package com.example.simplenotesapp

import androidx.lifecycle.ViewModel
import com.example.simplenotesapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    fun addNote(text: String) {
        val id = _notes.value.size + 1
        val note = Note(id, text)
        _notes.value += note
    }

    fun removeNote(id: Int) {
        val updatedList = _notes.value.filter { it.id != id }
        _notes.value = updatedList
    }
}