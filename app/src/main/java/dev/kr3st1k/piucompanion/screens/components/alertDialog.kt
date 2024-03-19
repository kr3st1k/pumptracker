package dev.kr3st1k.piucompanion.screens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyAlertDialog(
    showDialog: Boolean,
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = {
                Text(text = content)
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    // Действие при нажатии на кнопку "Подтвердить"
                }) {
                    Text(text = "Ща зайду, пажжи")
                }
            }
        )
    }
}