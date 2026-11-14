package com.example.ds.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.preview.navigation.PreviewNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkTheme by rememberSaveable { mutableStateOf(systemDark) }

            AuraTheme(
                colorMode = if (isDarkTheme) AuraColorMode.Dark else AuraColorMode.Light,
            ) {
                val auraColors = AuraTheme.colors
                // Temporary minimal MaterialTheme bridge until Steps 2-5 replace TopAppBar/FilterChip/Switch/Card.
                val compatScheme = if (isDarkTheme) {
                    darkColorScheme(
                        primary = auraColors.brand.primary,
                        onPrimary = auraColors.brand.onPrimary,
                        background = auraColors.background.primary,
                        onBackground = auraColors.content.primary,
                        surface = auraColors.surface.default,
                        onSurface = auraColors.content.primary,
                        surfaceVariant = auraColors.surface.raised,
                        onSurfaceVariant = auraColors.content.secondary,
                        secondaryContainer = auraColors.interaction.selectedContainer,
                        onSecondaryContainer = auraColors.interaction.onSelected,
                        outline = auraColors.border.default,
                    )
                } else {
                    lightColorScheme(
                        primary = auraColors.brand.primary,
                        onPrimary = auraColors.brand.onPrimary,
                        background = auraColors.background.primary,
                        onBackground = auraColors.content.primary,
                        surface = auraColors.surface.default,
                        onSurface = auraColors.content.primary,
                        surfaceVariant = auraColors.surface.raised,
                        onSurfaceVariant = auraColors.content.secondary,
                        secondaryContainer = auraColors.interaction.selectedContainer,
                        onSecondaryContainer = auraColors.interaction.onSelected,
                        outline = auraColors.border.default,
                    )
                }
                MaterialTheme(colorScheme = compatScheme) {
                    PreviewNavGraph(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme },
                    )
                }
            }
        }
    }
}
