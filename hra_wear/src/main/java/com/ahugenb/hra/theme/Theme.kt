package com.ahugenb.hra.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = Colors(
    primary = LightMahogany,
    primaryVariant = Mahogany,
    secondary = Mahogany,
    surface = Black,
    background = Black,
    onSurface = White,
    onPrimary = Black,
    onSecondary = Black
)

private val LightColorPalette = Colors(
    primary = Mahogany,
    primaryVariant = LightMahogany,
    secondary = LightMahogany,
    background = Mahogany,
)

@Composable
fun HraWearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}