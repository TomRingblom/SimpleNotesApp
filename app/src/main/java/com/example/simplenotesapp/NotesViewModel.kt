package com.example.simplenotesapp

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.data.NoteRepository
import com.example.simplenotesapp.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class NotesViewModel(
    private val noteRepository: NoteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _openAlertDialog = MutableLiveData(false)
    val openAlertDialog: LiveData<Boolean> = _openAlertDialog

    private val _isLinearLayout = MutableStateFlow(false)
    val isLinearLayout: StateFlow<Boolean> = userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
        isLinearLayout
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

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

    fun updateAlertDialog(show: Boolean) {
        _openAlertDialog.value = show
    }

    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
        _isLinearLayout.value = isLinearLayout
    }
}

data class NoteUiState(
    val noteList: List<Note> = listOf(),
    val isLinearLayout: Boolean = false
)