package com.example.simplenotesapp.ui.note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.NoteDto
import com.example.simplenotesapp.data.NoteRepository
import com.example.simplenotesapp.data.toNoteDto
import com.example.simplenotesapp.ui.theme.NoteEditDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository) : ViewModel() {


    private val itemId: Int = checkNotNull(savedStateHandle[NoteEditDestination.noteIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<NoteEditUiState> =
        noteRepository.getNoteStream(itemId)
            .filterNotNull()
            .map {
                NoteEditUiState(noteDto = it.toNoteDto())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteEditUiState()
            )

    suspend fun editNoteById(id: Int?, text: String) {
        val note = noteRepository.getNoteStream(id!!).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(text = text))
        } else {
            Log.i("Note not edited!", "Note with id $id not found.")
        }
    }
}

data class NoteEditUiState(val noteDto: NoteDto = NoteDto())