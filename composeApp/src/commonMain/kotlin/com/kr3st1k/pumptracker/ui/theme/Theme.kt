package com.kr3st1k.pumptracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PIUCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    paletteStyle: PaletteStyle = PaletteStyle.TonalSpot,
    seedColor: Color = Color.Blue,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        content = {
            DynamicMaterialTheme(
                animate = true,
                seedColor = seedColor,
                useDarkTheme = darkTheme,
                style = paletteStyle,
                content = content
            )
        }
    )
}