package dev.kr3st1k.piucompanion.ui.components.home.avatars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.kr3st1k.piucompanion.core.helpers.Utils.removeUrlParameters
import dev.kr3st1k.piucompanion.core.network.data.avatar.AvatarItem

@Composable
fun AvatarCard(avatar: AvatarItem, action: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .width(100.dp)
            .heightIn(min = 120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                action()
            },
        border = if (avatar.isSelected) BorderStroke(
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
                .background(color = Color(0xFF222933)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = removeUrlParameters(avatar.avatarUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
            )
            Text(
                modifier = Modifier.basicMarquee(),
                text = avatar.name,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (avatar.price != "0") "$${avatar.price}" else if (avatar.isSelected) "In use" else "Owned",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = if (avatar.price != "0") Color.Green else if (avatar.isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(
                    0.6f
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}