package com.example.ds.foundation.motion

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * Reads the system-wide Reduced Motion preference (`Settings.Global.ANIMATOR_DURATION_SCALE == 0f`).
 *
 * Returns `false` inside Compose tooling previews (`LocalInspectionMode`).
 */
@Composable
fun rememberAuraReducedMotion(): Boolean {
    if (LocalInspectionMode.current) return false
    val context = LocalContext.current
    return remember(context) {
        runCatching {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }.getOrDefault(false)
    }
}
