package com.example.simplenotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplenotesapp.ui.theme.SimpleNotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNotesAppTheme {
                SimpleNotesApp()
            }
        }
    }
}

@Composable
fun SimpleNotesApp() {
    Box(modifier = Modifier.fillMaxSize()) {
        val viewModel: NotesViewModel = viewModel()
        val notes = viewModel.notes.collectAsState().value
//        val notes = listOf("Hello World!", "Hello Kotlin!", "Hello Android!")
        var text by remember { mutableStateOf("")}
        val openAlertDialog = remember { mutableStateOf(false) }
        var noteToDelete = remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(32.dp)
        ) {
            notes.forEach { note ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = note.text,
                        modifier = Modifier.weight(1f),
                        fontSize = 24.sp
                    )
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Remove",
                        modifier = Modifier
                            .clickable {
                                noteToDelete.value = note.id
                                openAlertDialog.value = true
                            }
                    )
                }
            }
        }

        Column(modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it},
                label = { Text("Add a note")},
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if(text.isNotEmpty()) {
                        viewModel.addNote(text)
                        text = ""
                    }
                }
            ) {
                Text(text = "Add Note")
            }
        }

        when {
            openAlertDialog.value -> {
                AlertDialog(
                    title = { Text(text = "Delete Note") },
                    text = {
                        Text(text = "Are you sure you want to delete this note?")
                    },
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openAlertDialog.value = false
                                viewModel.removeNote(noteToDelete.value)
                            }) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { openAlertDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}

//@Composable
//fun AddNote(modifier: Modifier = Modifier) {
//
//}

@Preview(showBackground = true)
@Composable
fun SimpleNotesPreview() {
    SimpleNotesAppTheme {
        SimpleNotesApp()
    }
}