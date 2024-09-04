package com.example.simplenotesapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.model.Note
import com.example.simplenotesapp.navigation.Screen

@Composable
fun HomeScreen(viewModel: NotesViewModel, navController: NavHostController,) {
    Box(modifier = Modifier.fillMaxSize()) {
        val notes = viewModel.notes.collectAsState().value
        val openAlertDialog by viewModel.openAlertDialog.observeAsState(false)

        NoteList(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier.align(Alignment.TopCenter),
            notes = notes
        )
        AddNote(
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.Center)
        )
        when {
            openAlertDialog -> {
                NoteAlertDialog(viewModel)
            }
        }
    }
}

@Composable
fun NoteList(
    viewModel: NotesViewModel,
    navController: NavHostController,
    notes: List<Note>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(32.dp)
    ) {
        notes.forEach { note ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = note.text,
                    modifier = Modifier.weight(1f),
                    fontSize = 24.sp
                )
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .clickable {
                            Log.i("Navigation in Icon:", "${Screen.Edit.name}/${note.id}")
                            navController.navigate(route = "${Screen.Edit.name.toString()}/${note.id}")
                        }
                )
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.remove),
                    modifier = Modifier
                        .clickable {
                            viewModel.noteToDelete = note.id
                            viewModel.updateAlertDialog(true)
                        }
                )
            }
        }
    }
}

private fun addNoteToViewModel(viewModel: NotesViewModel, note: String) {
    viewModel.addNote(note)
}

@Composable
fun AddNote(viewModel: NotesViewModel, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    Column(modifier = modifier.padding(32.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it},
            label = { Text(stringResource(R.string.add_a_note)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(text.isNotEmpty()) {
                        addNoteToViewModel(viewModel, text)
                        text = ""
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if(text.isNotEmpty()) {
                    addNoteToViewModel(viewModel, text)
                    text = ""
                }
            }
        ) {
            Text(stringResource(R.string.add_note))
        }
    }
}

@Composable
fun NoteAlertDialog(viewModel: NotesViewModel) {
    AlertDialog(
        icon = {
            val iconColor = Color(0xFFF07167)
            Icon(
                imageVector =  Icons.Filled.Warning,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = iconColor
            )
        },
        title = {
            Text(stringResource(R.string.delete_note))
        },
        text = {
            Text(stringResource(R.string.alert_dialog_delete_note_question))
        },
        onDismissRequest = { viewModel.updateAlertDialog(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.updateAlertDialog(false)
                    viewModel.removeNote(viewModel.noteToDelete)
                }) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.updateAlertDialog(false)
                }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}