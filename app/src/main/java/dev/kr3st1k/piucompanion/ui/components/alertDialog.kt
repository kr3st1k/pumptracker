package dev.kr3st1k.piucompanion.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogWithButton(
    showDialog: Boolean,
    title: String,
    content: String,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = {
                Text(text = content)
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "OK")
                }
            }
        )
    }
}

@Composable
fun AlertDialogWithTwoButton(
    showDialog: Boolean,
    title: String,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = {
                Text(text = content)
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}