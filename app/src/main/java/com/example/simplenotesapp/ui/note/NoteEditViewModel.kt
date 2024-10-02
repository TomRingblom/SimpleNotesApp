package com.example.simplenotesapp.ui.note

import android.icu.text.SimpleDateFormat
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
import com.example.simplenotesapp.ui.theme.NoteEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

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

//    suspend fun editNoteById(id: Int?, text: String) {
//        val note = noteRepository.getNoteStream(id ?: 0).firstOrNull()
//        if (note != null) {
//            noteRepository.updateNote(note.copy(text = text))
//        } else {
//            Log.i("Note not edited!", "Note with id $id not found.")
//        }
//    }

    suspend fun saveNote(title: String, text: String, color: Long) {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        noteRepository.insertNote(
            Note(
            title = title,
            text = text,
            date = formatter.format(currentDate),
            color = color)
        )
    }

    fun updateUiState(noteDto: NoteDto) {
        uiState = NoteEditUiState(note = noteDto)
    }
}

data class NoteEditUiState(val note: NoteDto = NoteDto())