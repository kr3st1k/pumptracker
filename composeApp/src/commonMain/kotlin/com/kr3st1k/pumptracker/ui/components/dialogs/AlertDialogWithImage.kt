package com.kr3st1k.pumptracker.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage

@Composable
fun AlertDialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    uri: String,
    imageDescription: String,
) {
    var isConfirmed by remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = {
        if (!isConfirmed) {
            onDismissRequest()
        }
    }) {
        val colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
        Card(
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(160.dp)
                )
                Text(
                    text = imageDescription,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    if (!isConfirmed) {
                        TextButton(
                            onClick = { onDismissRequest() },
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                onConfirmation()
                                isConfirmed = true
                            },
                        ) {
                            Text("Confirm")
                        }
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}