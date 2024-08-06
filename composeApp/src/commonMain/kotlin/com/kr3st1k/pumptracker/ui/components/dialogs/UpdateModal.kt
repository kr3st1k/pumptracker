package com.kr3st1k.pumptracker.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl.downloadUpdate
import com.kr3st1k.pumptracker.getDownloadExtension
import com.kr3st1k.pumptracker.getDownloadsFolder
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.openFile
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

@Composable
fun UpdateModal(
    updateLink: MutableState<String>,
    onSkipClick: () -> Unit,
    changelog: String,
) {
    val scope = rememberCoroutineScope()
    var hideButtons by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0F) }
    val fileName = getDownloadsFolder().resolve("update${Clock.System.now().toEpochMilliseconds()}${getDownloadExtension(getPlatform())}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "A new update is available.")

        Card(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Changelog:",
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = changelog,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
        }

        Text(text = "By pressing SKIP, you agree to instability")

        Row(
           horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            if (!hideButtons) {
                Button(
                    onClick = {
                        hideButtons = true
                        scope.launch {
                            downloadUpdate(updateLink.value, fileName) { bytesDownloaded, contentLength ->
                                if (contentLength > 0 && bytesDownloaded != contentLength) {
                                    progress = (bytesDownloaded.toFloat() / contentLength) * 100
                                } else if (bytesDownloaded == contentLength) {
                                    openFile(fileName)
                                    println("wow")
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Update")
                }
                Button(
                    onClick = onSkipClick,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Skip")
                }
            } else {
                Box(
                    modifier = Modifier.padding(top=16.dp),
                ) {
                    LinearProgressIndicator(
                        progress = { progress }
                    )
                }
            }
        }

    }
}