package com.example.simplenotesapp.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simplenotesapp.NotesViewModel
import com.example.simplenotesapp.R
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAddScreen(viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory), navController: NavHostController) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val colors = listOf(Pair(0xFFFFDADB, "Pink"), Pair(0xFFD1EAED, "Turquoise"), Pair(0xFFFFD4AA, "Orange"), Pair(0xFFFDF3B4, "Yellow"))
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var selectedColor by remember {
        mutableStateOf(colors[0].second)
    }

    Column(modifier = Modifier.padding(32.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it},
            label = { Text(stringResource(R.string.title)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Text") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(text.isNotEmpty() && title.isNotEmpty()) {
                        coroutineScope.launch {
                            val color = colors.find { it.second == selectedColor }!!.first
                            viewModel.saveNote(title, text, color)
                            title = ""
                            text = ""
                            navController.popBackStack(Screen.Home.route, inclusive = false)
                        }
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedColor,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
            )
            
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                colors.forEachIndexed { index, color ->
                    DropdownMenuItem(
                        text = { Text(text = color.second) },
                        onClick = {
                            selectedColor = colors[index].second
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Button(
            onClick = {
                if(text.isNotEmpty() && title.isNotEmpty()) {
                    coroutineScope.launch {
                        val color = colors.find { it.second == selectedColor }!!.first
                        viewModel.saveNote(title, text, color)
                        title = ""
                        text = ""
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                }
            }
        ) {
            Text(stringResource(R.string.add_note))
        }
    }
}