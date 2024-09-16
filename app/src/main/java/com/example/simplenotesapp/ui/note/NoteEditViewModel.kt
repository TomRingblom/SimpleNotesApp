package com.example.simplenotesapp.ui.note

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.simplenotesapp.data.NoteRepository
import kotlinx.coroutines.flow.firstOrNull

class NoteEditViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    suspend fun editNoteById(id: Int?, text: String) {
        val note = noteRepository.getNoteStream(id!!).firstOrNull()
        if (note != null) {
            noteRepository.updateNote(note.copy(text = text))
        } else {
            Log.i("Note not edited!", "Note with id $id not found.")
        }
    }
}