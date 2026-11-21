package io.github.bluemarlins.aura.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.foundation.motion.AuraMotionDefaults
import io.github.bluemarlins.aura.foundation.theme.AuraMaterial
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class TaskScreenComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun floatingActionBar_rendersActionsAndSupportingText() {
        var saved = 0
        composeTestRule.setContent {
            AuraTheme {
                Box(Modifier.fillMaxSize()) {
                    AuraFloatingActionBar(supportingText = "Add a rating to save") {
                        AuraButton(text = "Save", onClick = { saved++ }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(AuraFloatingActionBarTestTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Add a rating to save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()
        assertEquals(1, saved)
    }

    @Test
    fun floatingActionBar_hidesWhenNotVisibleUnderReducedMotion() {
        var visible by mutableStateOf(true)
        composeTestRule.setContent {
            AuraTheme(motion = AuraMotionDefaults.Standard.copy(reduceMotion = true)) {
                Box(Modifier.fillMaxSize()) {
                    AuraFloatingActionBar(visible = visible) {
                        AuraButton(text = "Save", onClick = {})
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(AuraFloatingActionBarTestTag).assertExists()
        visible = false
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(AuraFloatingActionBarTestTag).assertDoesNotExist()
    }

    @Test
    fun floatingActionBar_fallsBackToSolidUnderSolidTheme() {
        composeTestRule.setContent {
            AuraTheme(material = AuraMaterial.Solid) {
                Box(Modifier.fillMaxSize()) {
                    AuraFloatingActionBar {
                        AuraButton(text = "Save", onClick = {})
                    }
                }
            }
        }

        composeTestRule
            .onNodeWithTag("aura_glass_surface_solid_fallback", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun skeleton_rendersBlocksAndAnnouncesLoading() {
        composeTestRule.setContent {
            AuraTheme(motion = AuraMotionDefaults.Standard.copy(reduceMotion = true)) {
                Box {
                    AuraSkeleton(
                        modifier = Modifier.width(120.dp).height(40.dp),
                        contentDescription = "Loading summary",
                    )
                }
            }
        }

        composeTestRule.onNode(hasContentDescription("Loading summary")).assertExists()
    }

    @Test
    fun skeletonText_rendersOneBlockPerLine() {
        composeTestRule.setContent {
            AuraTheme {
                Box {
                    AuraSkeletonText(lines = 3)
                }
            }
        }

        composeTestRule.onAllNodesWithTag(AuraSkeletonTestTag, useUnmergedTree = true)
            .fetchSemanticsNodes()
            .size
            .let { assertEquals(3, it) }
    }
}
