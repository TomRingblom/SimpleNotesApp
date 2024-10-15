package com.example.simplenotesapp.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.data.ColorRepository
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import com.example.simplenotesapp.ui.components.ColorDropDown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteAddScreen(
    viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val colors = ColorRepository().getColors()
    var selectedColor by remember {
        mutableStateOf(colors[0].second)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        val colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Black,
            focusedLabelColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title)) },
            maxLines = 1,
            colors = colors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Text") },
            maxLines = 1,
            colors = colors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(fieldsNotEmpty(title, text)) {
                        coroutineScope.launch {
                            saveNoteAndNavigate(coroutineScope, title, text,
                                selectedColor, viewModel, navController)
                        }
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))
        ColorDropDown(selectedColor = selectedColor, onColorSelect = { selectedColor = it })
        Button(
            onClick = {
                if(fieldsNotEmpty(title, text)) {
                    coroutineScope.launch {
                        saveNoteAndNavigate(coroutineScope, title, text,
                            selectedColor, viewModel, navController)
                    }
                }
            }
        ) {
            Text(stringResource(R.string.add_note))
        }
    }
}

private fun fieldsNotEmpty(title: String, text: String): Boolean {
    return title.isNotEmpty() && text.isNotEmpty()
}

private fun saveNoteAndNavigate(
    coroutineScope: CoroutineScope,
    title: String,
    text: String,
    selectedColor: String,
    viewModel: NotesViewModel,
    navController: NavHostController
) {
    coroutineScope.launch {
        val color = ColorRepository().getColors().find { it.second == selectedColor }!!.first
        viewModel.saveNote(title, text, color)
        navController.popBackStack(Screen.Home.route, inclusive = false)
    }
}