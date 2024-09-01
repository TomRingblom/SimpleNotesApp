package com.example.simplenotesapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.simplenotesapp.NotesViewModel

@Composable
fun EditScreen(viewModel: NotesViewModel, id: Int?, modifier: Modifier = Modifier) {
    val notes = viewModel.notes.collectAsState().value
    val note = notes.firstOrNull { it.id == id}
    Column {
        Text(text = "Navigated id: $id")
        Text(text = "Text of navigated id from the viewmodel: ${note?.text}")
    }
}

