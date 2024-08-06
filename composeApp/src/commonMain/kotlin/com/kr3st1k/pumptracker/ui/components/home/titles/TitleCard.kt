package com.kr3st1k.pumptracker.ui.components.home.titles

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.network.data.title.TitleItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TitleCard(title: TitleItem, action: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .heightIn(min = 80.dp)
            .clickable {
                action()
            },
        border = if (title.isSelected) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else BorderStroke(2.dp, Color(0xFF222933)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF222933)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .fillMaxWidth()
                .background(color = Color(0xFF222933))
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = title.name,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (!title.isAchieved) "Not completed" else if (title.isSelected) "In use" else "Owned",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = if (title.isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(
                    0.6f
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (title.progressValue != null && title.titleInfo != null && title.progress != null)
                if (title.progress != 0F) {

                    Text(
                        text = title.progressValue!!,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        color = Color.White.copy(0.6f),
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    LinearProgressIndicator(
                        progress = { title.progress!! },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                }
            Text(
                modifier = Modifier.basicMarquee(),
                text = title.description,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}