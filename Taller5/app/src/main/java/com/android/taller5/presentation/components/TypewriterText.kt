package com.android.taller5.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    delayMillis: Long = 20L
) {
    var textToDisplay by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        textToDisplay = ""
        text.forEach { char ->
            textToDisplay += char
            delay(delayMillis)
        }
    }

    Text(
        text = textToDisplay,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}
