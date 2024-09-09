package com.example.simplenotesapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplenotesapp.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes
    private val _openAlertDialog = MutableLiveData(false)
    val openAlertDialog: LiveData<Boolean> = _openAlertDialog
    var noteToDelete = 0

    fun addNote(text: String) {
        val id = _notes.value.size + 1
        val note = Note(id, text)
        _notes.value += note
    }

    fun removeNote(id: Int) {
        val updatedList = _notes.value.filter { it.id != id }
        _notes.value = updatedList
    }

    fun editNoteById(id: Int?, text: String) {
        val index = _notes.value.indexOfFirst { it.id == id }

        if (index != -1) {
            val currentList = _notes.value.toMutableList()
            val updatedNote = currentList[index].copy(text = text)
            currentList[index] = updatedNote
            _notes.value = currentList
        } else {
            Log.i("Note not created!", "Note with id $id not found.")
        }
    }

    fun updateAlertDialog(show: Boolean) {
        _openAlertDialog.value = show
    }
}