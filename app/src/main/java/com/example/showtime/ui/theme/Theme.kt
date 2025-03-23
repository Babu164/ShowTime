package com.example.showtime.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Gold80,      // Gold for dark mode accents
    secondary = Red80,     // Red for secondary elements
    tertiary = Black80     // Deep black for contrast
)

private val LightColorScheme = lightColorScheme(
    primary = Gold40,      // Darker gold in light mode for contrast
    secondary = Red40,     // Classic deep red
    tertiary = Black40     // Slightly lighter black for balance

    /* Other default colors to override
    background = Color(0xFF1C1C1C),  // Black for a cinema-style background
    surface = Color(0xFF2F2F2F),     // Darker gray for contrast
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    */
)

@Composable
fun ShowTimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
