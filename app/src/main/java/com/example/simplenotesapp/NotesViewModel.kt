package com.example.simplenotesapp

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import java.util.Locale

class NotesViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes
    private val _openAlertDialog = MutableLiveData(false)
    val openAlertDialog: LiveData<Boolean> = _openAlertDialog
    var noteToDelete = 0

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val noteUiState: StateFlow<NoteUiState> = noteRepository.getAllNotesStream().map { NoteUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = NoteUiState()
    )

    suspend fun saveNote(title: String, text: String, color: Long) {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        noteRepository.insertNote(Note(
            title = title,
            text = text,
            date = formatter.format(currentDate),
            color = color)
        )
    }

    suspend fun removeNote(noteId: Int) {
        val note = noteRepository.getNoteStream(noteId).firstOrNull()
        if (note != null) {
            noteRepository.deleteNote(note)
        } else {
            Log.i("Note not removed!", "Note with id $noteId not found.")
        }
    }

    suspend fun editNoteById(id: Int?, text: String) {
        val note = noteRepository.getNoteStream(id!!).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(text = text))
        } else {
            Log.i("Note not edited!", "Note with id $id not found.")
        }
    }

    fun updateAlertDialog(show: Boolean) {
        _openAlertDialog.value = show
    }
}

data class NoteUiState(val noteList: List<Note> = listOf())