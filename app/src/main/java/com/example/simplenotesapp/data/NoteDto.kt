package com.example.simplenotesapp.data

data class NoteDto(
    val id: Int = 0,
    val title: String = "",
    val text: String = "",
    val color: Long = 0xFFD1EAED,
    val date: String = ""
)

fun NoteDto.toNote(): Note = Note(
    id = id,
    title = title,
    text = text,
    color = color,
    date = date
)