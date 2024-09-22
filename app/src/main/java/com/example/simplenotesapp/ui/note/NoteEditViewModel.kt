package com.example.simplenotesapp.ui.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.NoteDto
import com.example.simplenotesapp.data.NoteRepository
import com.example.simplenotesapp.ui.theme.NoteEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {
    var uiState by mutableStateOf(NoteEditUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[NoteEditDestination.noteIdArg] ?: 0)

    init {
        viewModelScope.launch {
            uiState = noteRepository.getNoteStream(itemId)
                .filterNotNull()
                .map {
                    NoteEditUiState(note = NoteDto(it.id, it.text))
                }.first()
        }
    }

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