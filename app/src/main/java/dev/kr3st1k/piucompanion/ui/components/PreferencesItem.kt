package dev.kr3st1k.piucompanion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwitchPreferenceItem(
    label: String,
    initialState: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clickable {
                onCheckedChange(!initialState)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = label)
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun RadioPreferenceItem(
    label: String,
    initialState: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier
        .padding(vertical = 16.dp)
        .clickable {
            onCheckedChange(!initialState)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = initialState,
                onClick = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }
}