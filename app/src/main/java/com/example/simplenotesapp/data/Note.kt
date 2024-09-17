package com.example.simplenotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)

fun Note.toNoteDto(): NoteDto = NoteDto(id = id, text = text)