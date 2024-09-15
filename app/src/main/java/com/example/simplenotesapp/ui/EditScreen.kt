package com.example.simplenotesapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditScreen(
    viewModel: NotesViewModel,
    navController: NavHostController,
    id: String?,
    modifier: Modifier = Modifier
) {
    val noteUiState by viewModel.noteUiState.collectAsState()
    val note = noteUiState.noteList.firstOrNull { it.id == id?.toInt()}
    var text by remember { mutableStateOf(note!!.text) }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier
        .fillMaxSize()
        .padding(10.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it},
            label = { Text(stringResource(R.string.edit_note))},
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    saveNote(
                        viewModel = viewModel,
                        id = note!!.id,
                        text = text,
                        navController = navController,
                        coroutineScope = coroutineScope
                    )
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        Button(
            onClick = {
                saveNote(
                    viewModel = viewModel,
                    id = note!!.id,
                    text = text,
                    navController = navController,
                    coroutineScope = coroutineScope
                )
            }
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

private fun saveNote(
    viewModel: NotesViewModel,
    id: Int,
    text: String,
    navController:
    NavHostController,
    coroutineScope: CoroutineScope
) {
    if(text.isNotEmpty()) {
        coroutineScope.launch {
            viewModel.editNoteById(id, text)
            navController.popBackStack(Screen.Home.route, inclusive = false)
        }
    }
}

