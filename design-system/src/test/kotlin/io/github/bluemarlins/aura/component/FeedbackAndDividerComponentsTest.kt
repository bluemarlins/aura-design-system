package io.github.bluemarlins.aura.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.foundation.shape.AuraShapesDefaults
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class FeedbackAndDividerComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun auraShapes_exposesExpectedCornerTokens() {
        val shapes = AuraShapesDefaults.Standard
        assertEquals(RoundedCornerShape(4.dp), shapes.extraSmall)
        assertEquals(RoundedCornerShape(8.dp), shapes.small)
        assertEquals(RoundedCornerShape(12.dp), shapes.medium)
        assertEquals(RoundedCornerShape(16.dp), shapes.large)
        assertEquals(RoundedCornerShape(24.dp), shapes.extraLarge)
        assertEquals(CircleShape, shapes.full)
    }

    @Test
    fun auraDivider_rendersHorizontalAndVerticalVariants() {
        composeTestRule.setContent {
            AuraTheme {
                AuraDivider(
                    orientation = AuraDividerOrientation.Horizontal,
                    tone = AuraDividerTone.Subtle,
                    modifier = Modifier.testTag("divider_h"),
                )
                AuraDivider(
                    orientation = AuraDividerOrientation.Vertical,
                    tone = AuraDividerTone.Strong,
                    modifier = Modifier.testTag("divider_v"),
                )
            }
        }

        composeTestRule.onNodeWithTag("divider_h").assertExists()
        composeTestRule.onNodeWithTag("divider_v").assertExists()
    }

    @Test
    fun auraProgressBars_exposeBoundedRangeSemantics() {
        composeTestRule.setContent {
            AuraTheme {
                AuraLinearProgressBar(
                    progress = 1.4f,
                    modifier = Modifier.testTag("linear_progress"),
                    contentDescription = "Profile readiness",
                )
                AuraStepProgressBar(
                    currentStep = 3,
                    totalSteps = 5,
                    modifier = Modifier.testTag("step_progress"),
                    contentDescription = "Step 3 of 5",
                )
            }
        }

        composeTestRule.onNodeWithTag("linear_progress").assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ProgressBarRangeInfo,
                ProgressBarRangeInfo(current = 1f, range = 0f..1f),
            ),
        )
        composeTestRule.onNodeWithTag("step_progress").assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ProgressBarRangeInfo,
                ProgressBarRangeInfo(current = 3f, range = 0f..5f),
            ),
        )
    }

    @Test
    fun auraSnackbarHost_displaysMessageAndReturnsActionResult() {
        val hostState = AuraSnackbarHostState()
        var lastResult: AuraSnackbarResult? = null

        composeTestRule.setContent {
            val scope = rememberCoroutineScope()
            AuraTheme {
                AuraScaffold(
                    snackbarHost = {
                        AuraSnackbarHost(hostState = hostState)
                    },
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AuraButton(
                            text = "Trigger",
                            onClick = {
                                scope.launch {
                                    lastResult = hostState.showSnackbar(
                                        message = "Saved tasting note",
                                        actionLabel = "Undo",
                                        duration = AuraSnackbarDuration.Indefinite,
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Trigger").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Saved tasting note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Undo").performClick()
        composeTestRule.waitForIdle()

        assertEquals(AuraSnackbarResult.ActionPerformed, lastResult)
    }
}
