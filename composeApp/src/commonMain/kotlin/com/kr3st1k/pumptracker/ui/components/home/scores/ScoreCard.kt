package com.kr3st1k.pumptracker.ui.components.home.scores

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.data.score.LatestScore
import com.kr3st1k.pumptracker.core.db.data.score.Score
import com.kr3st1k.pumptracker.core.helpers.Utils
import com.kr3st1k.pumptracker.core.helpers.Utils.removeUrlParameters

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreCard(score: Score) {
    OutlinedCard(
        modifier = Modifier
            .width(220.dp)
            .padding(bottom = 4.dp)
            .height(115.dp)
            .heightIn(min = 115.dp),
        shape = RectangleShape,
    ) {

        Box {
            AsyncImage(
                model = score.songBackgroundUri?.let { removeUrlParameters(it) },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(end = 8.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = score.songName,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.basicMarquee(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = score.difficulty,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = Color(0xffd1cfcf),

                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(start = 8.dp)
                            .width(20.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = score.score,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = Color.White,

                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                        if (score is BestScore)
                            Text(
                                text = score.pumbilityScore.toString(),
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                color = Color.White.copy(0.7f),
                                textAlign = TextAlign.End
                            )
                        if (score.rank != "F")
                            Text(
                                text = score.rank,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                color = Color.White.copy(0.6f),

                                textAlign = TextAlign.End
                            )
                        if (score is LatestScore)
                            Text(
                                Utils.convertDateFromSite(score.datetime),
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                color = Color.White.copy(0.5f),
                                textAlign = TextAlign.End
                            )
                    }
                }
            }
        }
    }
}
