package com.example.simplenotesapp.data

data class NoteDto(val id: Int = 0, val text: String = "")

fun NoteDto.toNote(): Note = Note(id = id, text = text)