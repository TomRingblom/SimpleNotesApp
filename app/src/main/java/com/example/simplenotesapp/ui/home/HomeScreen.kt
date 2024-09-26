package com.example.simplenotesapp.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.SimpleNotesApp
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.data.NoteRepository
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import com.example.simplenotesapp.ui.theme.SimpleNotesAppTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory), navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        val openAlertDialog by viewModel.openAlertDialog.observeAsState(false)
        val noteUiState by viewModel.noteUiState.collectAsState()

        NoteList(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier.align(Alignment.TopCenter),
            notes = noteUiState.noteList
        )
        Button(
            onClick = {
            navController.navigate(Screen.Add.route)
        },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text(text = "Add a note")
        }
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
        val colors = listOf(0xFFFFDADB, 0xFFD1EAED, 0xFFFFD4AA, 0xFFFDF3B4)
        notes.forEach { note ->
            val randomNumber = Random.nextInt(colors.size)
            NoteItem(
                note = note,
                viewModel = viewModel,
                navController = navController,
                color = colors[randomNumber]
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    viewModel: NotesViewModel,
    navController: NavHostController,
    color: Long
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(color))
        .padding(8.dp)
    ) {
        Text(
            text = note.text,
            modifier = Modifier.weight(1f),
            fontSize = 24.sp
        )
        Icon(
            Icons.Filled.Edit,
            contentDescription = stringResource(R.string.edit),
            modifier = Modifier
                .clickable {
                    Log.i("NoteList", "noteId: ${note.id}")
                    navController.navigate(route = "${Screen.Edit.name}/${note.id}")
                }
        )
        Spacer(modifier = Modifier.padding(10.dp))
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

//@Preview(showBackground = true)
//@Composable
//fun NoteItemPreview() {
//    NoteItem(
//        note = Note(text = "This is a note"),
//        navController = NavHostController(LocalContext.current),
//        0xFFD1EAED
//    )
//}

@Composable
fun NoteAlertDialog(viewModel: NotesViewModel) {
    val coroutineScope = rememberCoroutineScope()
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
                    coroutineScope.launch {
                        viewModel.removeNote(viewModel.noteToDelete)
                    }
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