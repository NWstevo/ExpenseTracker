package com.expensetracker.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = OnGreenContainer,
    secondary = BluePrimary,
    onSecondary = Color.White,
    secondaryContainer = BluePrimaryContainer,
    onSecondaryContainer = OnBlueContainer,
    tertiary = BrownPrimary,
    onTertiary = Color.White,
    tertiaryContainer = BrownPrimaryContainer,
    onTertiaryContainer = OnBrownContainer,
    surface = SurfaceLight,
    background = SurfaceLight
)

@Composable
fun ExpenseTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
