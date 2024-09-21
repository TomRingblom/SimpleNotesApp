package com.example.simplenotesapp.ui.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.data.NoteDto
import com.example.simplenotesapp.data.NoteRepository
import com.example.simplenotesapp.data.toItemUiState
import com.example.simplenotesapp.data.toNoteDto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            uiState = noteRepository.getNoteStream(savedStateHandle["id"] ?: 0)
                .filterNotNull()
                .map {
                    NoteEditUiState(note = NoteDto(it.id, it.text))
                }.first()
        }
    }

    var uiState by mutableStateOf(NoteEditUiState())
        private set

    suspend fun editNoteById(id: Int?, text: String) {
        val note = noteRepository.getNoteStream(id ?: 0).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(text = text))
        } else {
            Log.i("Note not edited!", "Note with id $id not found.")
        }
    }

    fun updateUiState(noteDto: NoteDto) {
        uiState = NoteEditUiState(note = noteDto)
    }
}

data class NoteEditUiState(val note: NoteDto = NoteDto())