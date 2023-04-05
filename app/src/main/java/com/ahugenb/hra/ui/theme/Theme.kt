package com.ahugenb.hra.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = LightMahogany,
    primaryVariant = Mahogany,
    secondary = Mahogany,
    surface = Black,
    background = Black,
    onSurface = White,
    onPrimary = Black,
    onSecondary = Black
)

private val LightColorPalette = lightColors(
    primary = Mahogany,
    primaryVariant = LightMahogany,
    secondary = LightMahogany,
    background = Mahogany,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun HraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    
    systemUiController.setSystemBarsColor(colors.primary)
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}