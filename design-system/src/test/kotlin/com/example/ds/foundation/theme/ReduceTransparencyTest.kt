package com.example.ds.foundation.theme

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import com.example.ds.component.GlassSurface
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ReduceTransparencyTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun solidFallbackNodes() =
        composeTestRule.onAllNodesWithTag("aura_glass_surface_solid_fallback", useUnmergedTree = true)

    @Test
    @Config(sdk = [33])
    fun batterySaverToggledWhileRunning_switchesGlassToSolidFallback() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        composeTestRule.setContent {
            AuraTheme {
                GlassSurface(modifier = Modifier.size(120.dp, 60.dp)) {}
            }
        }
        composeTestRule.waitForIdle()
        solidFallbackNodes().assertCountEquals(0)

        shadowOf(powerManager).setIsPowerSaveMode(true)
        context.sendBroadcast(Intent(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED))
        shadowOf(android.os.Looper.getMainLooper()).idle()
        composeTestRule.waitForIdle()
        solidFallbackNodes().assertCountEquals(1)

        shadowOf(powerManager).setIsPowerSaveMode(false)
        context.sendBroadcast(Intent(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED))
        shadowOf(android.os.Looper.getMainLooper()).idle()
        composeTestRule.waitForIdle()
        solidFallbackNodes().assertCountEquals(0)
    }

    @Test
    @Config(sdk = [30])
    fun preBlurApi_rendersOpaqueSolidFallback() {
        composeTestRule.setContent {
            AuraTheme(reduceTransparency = false) {
                GlassSurface(modifier = Modifier.size(120.dp, 60.dp)) {}
            }
        }
        composeTestRule.waitForIdle()
        solidFallbackNodes().assertCountEquals(1)
    }

    @Test
    @Config(sdk = [33])
    fun solidThemeCeiling_rendersOpaqueSolidFallback() {
        composeTestRule.setContent {
            AuraTheme(material = AuraMaterial.Solid, reduceTransparency = false) {
                GlassSurface(modifier = Modifier.size(120.dp, 60.dp)) {}
            }
        }
        composeTestRule.waitForIdle()
        solidFallbackNodes().assertCountEquals(1)
    }
}
