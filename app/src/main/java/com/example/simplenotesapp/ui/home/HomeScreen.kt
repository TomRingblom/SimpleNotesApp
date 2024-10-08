package com.example.simplenotesapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Thin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.data.Note
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory), navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        val openAlertDialog by viewModel.openAlertDialog.observeAsState(false)
        val useGridLayout by viewModel.useGridLayout.observeAsState(false)
        val noteUiState by viewModel.noteUiState.collectAsState()

        NoteList(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier.align(Alignment.TopCenter),
            notes = noteUiState.noteList,
            useGridLayout = useGridLayout
        )
        Button(
            onClick = {
                navController.navigate(Screen.Add.route)
            },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .align(Alignment.BottomCenter),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF212121),
                contentColor = Color(0xFFCED4DA)
            )
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_note)
            )
        }
        Button(
            onClick = { viewModel.updateGridLayout() },
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Text("Grid layout")
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
    modifier: Modifier = Modifier,
    useGridLayout: Boolean
) {
    if(useGridLayout) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .padding(16.dp)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    viewModel = viewModel,
                    navController = navController
                )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    viewModel: NotesViewModel,
    navController: NavHostController
) {
    Column(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(Color(note.color))
        .padding(8.dp)
        .clickable {
            navController.navigate(route = "${Screen.Edit.name}/${note.id}")
        }
    ) {
        Row {
            Text(
                text = note.title,
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))
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
        Spacer(modifier = Modifier.padding(1.dp))
        Text(
            text = note.text,
            fontSize = 16.sp,
            fontWeight = Thin
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = note.date,
            fontSize = 8.sp,
            fontWeight = Bold
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NoteItemPreview() {
//    NoteItem(
//        note = Note(title = "Hello", text = "This is a note", color = 0xFFFFDADB, date = "2024-10-04"),
//        navController = NavHostController(LocalContext.current)
//    )
//}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    Button(
        onClick = {},
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF212121),
            contentColor = Color(0xFFCED4DA)
        )
    ) {
//        Text(text = "Add a note")
        Icon(
            Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_note)
        )
    }
}

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