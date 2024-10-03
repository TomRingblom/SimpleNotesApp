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
                    NoteEditUiState(note = NoteDto(
                        id = it.id,
                        title = it.title,
                        text = it.text,
                        date = it.date,
                        color = it.color))
                }.first()
        }
    }

    suspend fun editNoteById(dto: NoteDto) {
        val note = noteRepository.getNoteStream(dto.id ?: 0).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(
                title = dto.title,
                text = dto.text,
                color = dto.color
            ))
        } else {
            Log.i("Note not edited!", "Note not found.")
        }
    }

    fun updateUiState(noteDto: NoteDto) {
        uiState = NoteEditUiState(note = noteDto)
    }
}

data class NoteEditUiState(val note: NoteDto = NoteDto())