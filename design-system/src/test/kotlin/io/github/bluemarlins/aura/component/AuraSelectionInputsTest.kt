package io.github.bluemarlins.aura.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
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
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w411dp-h891dp")
class AuraSelectionInputsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val textField = SemanticsMatcher.keyIsDefined(SemanticsActions.SetText)

    @Test
    fun `search bar input spans the whole capsule so a tap on its padding focuses it`() {
        composeTestRule.setContent {
            AuraTheme {
                AuraSearchBar(query = "", onQueryChange = {}, placeholder = "Search drinks")
            }
        }

        val field = composeTestRule.onNode(textField)
        val heightDp = with(composeTestRule.density) { field.fetchSemanticsNode().size.height.toDp() }
        assertTrue("input should cover the 52dp bar, was $heightDp", heightDp >= 52.dp)

        // 4dp below the top edge: outside the text line, inside the capsule.
        field.performTouchInput { click(androidx.compose.ui.geometry.Offset(centerX, 4.dp.toPx())) }
        field.assertIsFocused()
    }

    @Test
    fun `segmented button can show no selection and select later`() {
        var selected by mutableIntStateOf(-1)
        composeTestRule.setContent {
            AuraTheme {
                AuraSegmentedButton(
                    options = listOf("Dry", "Medium", "Sweet"),
                    selectedIndex = selected,
                    onOptionSelected = { selected = it },
                )
            }
        }

        val selectedNodes = composeTestRule.onAllNodes(
            SemanticsMatcher.expectValue(SemanticsProperties.Selected, true),
        ).fetchSemanticsNodes()
        assertEquals("no segment is selected before an answer", 0, selectedNodes.size)

        composeTestRule.onNodeWithText("Sweet").performTouchInput { click() }
        composeTestRule.waitForIdle()

        assertEquals(2, selected)
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.Role, androidx.compose.ui.semantics.Role.Tab) and
                SemanticsMatcher.expectValue(SemanticsProperties.Selected, true),
        ).assertIsSelected()
    }
}
