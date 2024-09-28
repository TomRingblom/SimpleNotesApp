package com.example.simplenotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplenotesapp.NoteUiState
import com.example.simplenotesapp.ui.note.NoteEditUiState

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val text: String,
    val color: Long,
    val date: String
)

fun Note.toNoteDto(): NoteDto = NoteDto(id = id, text = text)

fun Note.toItemUiState(): NoteEditUiState = NoteEditUiState(
    note = this.toNoteDto()
)