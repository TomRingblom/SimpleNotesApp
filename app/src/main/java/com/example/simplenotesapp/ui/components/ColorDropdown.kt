package com.example.simplenotesapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplenotesapp.data.ColorRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropDown(selectedColor: String, onColorSelect: (String) -> Unit) {
    val colors = ColorRepository().getColors()
    var isExpanded by remember {
        mutableStateOf(false)
    }

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
                    text = { RowWithColorCircleAndText(color = color.first, text = color.second) },
                    onClick = {
                        onColorSelect(colors[index].second)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun RowWithColorCircleAndText(color: Long, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier
            .size(16.dp)
        ) {
            drawCircle(
                color = Color(color)
            )
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun ColorCircleTextPreview() {
    RowWithColorCircleAndText(color = 0xFFFFDADB, text = "Pink")
}