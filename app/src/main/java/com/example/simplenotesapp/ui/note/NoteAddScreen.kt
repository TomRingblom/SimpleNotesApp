package com.example.simplenotesapp.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NoteAddScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 32.dp)) {
        Text(text = "Note Add Screen")
        
    }
}