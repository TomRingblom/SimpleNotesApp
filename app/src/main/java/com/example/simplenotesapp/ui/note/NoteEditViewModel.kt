package com.example.simplenotesapp.ui.note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.data.NoteDto
import com.example.simplenotesapp.data.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository) : ViewModel() {

//    private val itemId: Int = savedStateHandle.get<Int>(NoteEditDestination.noteIdArg)!!
//    private val itemId: Int = checkNotNull(savedStateHandle[NoteEditDestination.noteIdArg])
    private val itemId: Int = 26
    val saved: Int? = savedStateHandle["id"]

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<NoteEditUiState> = noteRepository.getNoteStream(saved ?: 0)
        .filterNotNull()
        .map {
            NoteEditUiState(note = NoteDto(it.id, it.text))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = NoteEditUiState())

//            .filterNotNull()
//            .map {
//                NoteEditUiState(noteDto = it.toNoteDto())
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = NoteEditUiState()
//            )

    suspend fun editNoteById(id: Int?, text: String) {
        val note = noteRepository.getNoteStream(id!!).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(text = text))
        } else {
            Log.i("Note not edited!", "Note with id $id not found.")
        }
    }

    suspend fun getNoteById(id: Int) {
        noteRepository.getNoteStream(id)
            .filterNotNull()
            .map {
                Note(id = it.id, text = it.text)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = Note(text = ""))
    }
}

data class NoteEditUiState(val note: NoteDto = NoteDto())