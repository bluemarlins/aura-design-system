package io.github.bluemarlins.aura.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
// Phone-sized window: under 480dp of height the adaptive top bar switches to its compact docked form.
@Config(sdk = [33], qualifiers = "w411dp-h891dp")
class AuraDropdownMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val window = IntSize(1000, 2000)
    private val menu = IntSize(500, 600)

    @Test
    fun `menu opens below the anchor aligned to its end edge`() {
        val anchor = IntRect(left = 880, top = 100, right = 980, bottom = 200)

        val offset = dropdownMenuOffset(anchor, window, LayoutDirection.Ltr, menu, gapPx = 10, marginPx = 20)

        assertEquals(IntOffset(480, 210), offset)
    }

    @Test
    fun `menu stays inside the window margin when the anchor is near the start edge`() {
        val anchor = IntRect(left = 0, top = 100, right = 100, bottom = 200)

        val offset = dropdownMenuOffset(anchor, window, LayoutDirection.Ltr, menu, gapPx = 10, marginPx = 20)

        assertEquals(20, offset.x)
    }

    @Test
    fun `menu flips above the anchor when there is no room below`() {
        val anchor = IntRect(left = 880, top = 1700, right = 980, bottom = 1800)

        val offset = dropdownMenuOffset(anchor, window, LayoutDirection.Ltr, menu, gapPx = 10, marginPx = 20)

        assertEquals(1700 - 10 - 600, offset.y)
    }

    @Test
    fun `menu aligns to the anchor start in right-to-left layouts`() {
        val anchor = IntRect(left = 40, top = 100, right = 140, bottom = 200)

        val offset = dropdownMenuOffset(anchor, window, LayoutDirection.Rtl, menu, gapPx = 10, marginPx = 20)

        assertEquals(40, offset.x)
    }

    @Test
    fun `choosing an item reports it, exposes selection, and dismisses`() {
        var expanded by mutableStateOf(true)
        var picked: String? = null
        composeTestRule.setContent {
            AuraTheme {
                Box {
                    AuraDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        sections = listOf(
                            AuraMenuSection(
                                title = "Type",
                                items = listOf(
                                    AuraMenuItem(id = "all", label = "All", selected = true),
                                    AuraMenuItem(id = "wine", label = "Wine"),
                                ),
                            ),
                            AuraMenuSection(
                                title = "Sort",
                                items = listOf(AuraMenuItem(id = "rating", label = "Highest rated")),
                            ),
                        ),
                        onItemClick = { picked = it.id },
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sort").assertIsDisplayed()
        composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Selected, true))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Wine").performClick()
        composeTestRule.waitForIdle()

        assertEquals("wine", picked)
        assertTrue(!expanded)
    }

    @Test
    @GraphicsMode(GraphicsMode.Mode.NATIVE) // real text metrics; legacy mode inflates text heights and could fake the overlap
    fun `inline expanded title shares the row with actions instead of adding a second row`() {
        composeTestRule.setContent {
            AuraTheme {
                AuraTopAppBar(
                    title = "Journal",
                    expandedSubtitle = "Wine · Highest rated",
                    scrollBehavior = AuraTopAppBarScrollBehavior.AdaptiveFloating,
                    inlineExpandedTitle = true,
                    adaptiveState = null,
                    actions = { AuraText("Filter") },
                )
            }
        }

        val title = composeTestRule.onNodeWithText("Journal").fetchSemanticsNode().boundsInRoot
        val action = composeTestRule.onNodeWithText("Filter").fetchSemanticsNode().boundsInRoot
        composeTestRule.onNodeWithText("Wine · Highest rated").assertIsDisplayed()
        composeTestRule.onNodeWithText("Journal")
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
        // Same row: without the inline option the title sits in a second row entirely below the actions.
        assertTrue(
            "title $title and action $action should overlap vertically",
            title.top < action.bottom && action.top < title.bottom,
        )
    }
}
