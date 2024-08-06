package com.kr3st1k.pumptracker.ui.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
//import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBestScores(
    options: List<Pair<String, Int>>,
    selectedOption: Pair<String, Int>,
    onUpdate: (Pair<String, Int>) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption.first,
                onValueChange = {},
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults
                    .textFieldColors(),
                modifier = Modifier
                    .menuAnchor(PrimaryNotEditable, true)
                    .fillMaxWidth(),
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .height(475.dp)
                    .exposedDropdownSize()
                    .padding(horizontal = 16.dp),
            ) {
                options.forEach { (name, value) ->
                    DropdownMenuItem(
                        text = {
                            Text(text = name)
                        },
                        onClick = {
                            onUpdate(Pair(name, value))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}