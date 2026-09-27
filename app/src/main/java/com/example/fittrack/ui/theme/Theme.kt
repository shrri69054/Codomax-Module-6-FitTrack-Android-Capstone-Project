package com.example.fittrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FitTrackGreen = Color(0xFF2E7D32)
private val FitTrackGreenDark = Color(0xFF1B5E20)
private val FitTrackAccent = Color(0xFFFF6F00)

private val LightColors = lightColorScheme(
    primary = FitTrackGreen,
    secondary = FitTrackAccent,
    tertiary = FitTrackGreenDark
)

private val DarkColors = darkColorScheme(
    primary = FitTrackGreen,
    secondary = FitTrackAccent,
    tertiary = FitTrackGreenDark
)

@Composable
fun FitTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
