package com.example.simplenotesapp.ui.theme

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
import com.example.simplenotesapp.R
import com.example.simplenotesapp.data.ColorRepository
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import com.example.simplenotesapp.ui.components.ColorDropDown
import com.example.simplenotesapp.ui.navigation.NavigationDestination
import com.example.simplenotesapp.ui.note.NoteEditViewModel
import kotlinx.coroutines.launch

object NoteEditDestination : NavigationDestination {
    override val route: String = "Edit"
    override val titleRes: Int = R.string.edit_note
    const val noteIdArg = "id"
    val routeWithArgs = "$route/{$noteIdArg}"
}

@Composable
fun NoteEditScreen(
    viewModel: NoteEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val color = ColorRepository().getColors().find {
        it.first == viewModel.uiState.note.color
    }
    var selectedColor by remember {
        mutableStateOf(color?.second ?: "")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        val colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Black,
            focusedLabelColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black
        )
        OutlinedTextField(
            value = viewModel.uiState.note.title,
            onValueChange = {
                viewModel.updateUiState(viewModel.uiState.note.copy(title = it))
            },
            label = { Text(stringResource(R.string.edit_note)) },
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
        OutlinedTextField(
            value = viewModel.uiState.note.text,
            onValueChange = {
                viewModel.updateUiState(viewModel.uiState.note.copy(text = it))
            },
            label = { Text("Edit text") },
            maxLines = 1,
            colors = colors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(fieldsNotEmpty(viewModel.uiState.note.title, viewModel.uiState.note.text)) {
                        coroutineScope.launch {
                            val color = ColorRepository().getColors().find {
                                it.second == selectedColor
                            }!!.first
                            viewModel.updateUiState(viewModel.uiState.note.copy(color = color))
                            viewModel.editNoteById(viewModel.uiState.note)
                            navController.popBackStack(Screen.Home.route, inclusive = false)
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
                if(fieldsNotEmpty(viewModel.uiState.note.title, viewModel.uiState.note.text)) {
                    coroutineScope.launch {
                        val color = ColorRepository().getColors().find { it.second == selectedColor }!!.first
                        viewModel.updateUiState(viewModel.uiState.note.copy(color = color))
                        viewModel.editNoteById(viewModel.uiState.note)
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                }
            }
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

private fun fieldsNotEmpty(title: String, text: String): Boolean {
    return title.isNotEmpty() && text.isNotEmpty()
}

