package dev.kr3st1k.piucompanion.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PIUViewModel : ViewModel() {
    var uiState by mutableStateOf(PIUState())
        private set
}