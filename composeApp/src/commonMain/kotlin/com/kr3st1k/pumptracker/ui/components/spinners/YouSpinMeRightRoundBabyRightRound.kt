package com.kr3st1k.pumptracker.ui.components.spinners

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun YouSpinMeRightRoundBabyRightRound(
    text: String? = null,
    progress: Float? = null
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (progress == null)
            CircularProgressIndicator()
        else
            CircularProgressIndicator(
                progress = { progress }
            )
        if (text != null) {
            Text(
                text = text,
                modifier = Modifier.padding(top = 80.dp)
            )
        }
    }
}