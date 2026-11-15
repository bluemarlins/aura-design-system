package com.example.ds.preview.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ComponentCatalogTest {

    @Test
    fun `catalog item ids are unique`() {
        val ids = ComponentCatalog.items.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `color scheme, typography layout, button, and text field items are registered as ready`() {
        val colorScheme = ComponentCatalog.findById(ComponentCatalog.COLOR_SCHEME_ID)
        assertNotNull(colorScheme)
        assertEquals(CatalogStatus.Ready, colorScheme?.status)

        val typographyLayout = ComponentCatalog.findById(ComponentCatalog.TYPOGRAPHY_LAYOUT_ID)
        assertNotNull(typographyLayout)
        assertEquals(CatalogStatus.Ready, typographyLayout?.status)

        val button = ComponentCatalog.findById(ComponentCatalog.BUTTON_ID)
        assertNotNull(button)
        assertEquals(CatalogStatus.Ready, button?.status)

        val iconButton = ComponentCatalog.findById(ComponentCatalog.ICON_BUTTON_ID)
        assertNotNull(iconButton)
        assertEquals(CatalogStatus.Ready, iconButton?.status)

        val fab = ComponentCatalog.findById(ComponentCatalog.FAB_ID)
        assertNotNull(fab)
        assertEquals(CatalogStatus.Ready, fab?.status)

        val segmentedButton = ComponentCatalog.findById(ComponentCatalog.SEGMENTED_BUTTON_ID)
        assertNotNull(segmentedButton)
        assertEquals(CatalogStatus.Ready, segmentedButton?.status)

        val chipButton = ComponentCatalog.findById(ComponentCatalog.CHIP_BUTTON_ID)
        assertNotNull(chipButton)
        assertEquals(CatalogStatus.Ready, chipButton?.status)

        val textField = ComponentCatalog.findById(ComponentCatalog.TEXT_FIELD_ID)
        assertNotNull(textField)
        assertEquals(CatalogStatus.Ready, textField?.status)

        val checkbox = ComponentCatalog.findById(ComponentCatalog.CHECKBOX_ID)
        assertNotNull(checkbox)
        assertEquals(CatalogStatus.Ready, checkbox?.status)

        val switchItem = ComponentCatalog.findById(ComponentCatalog.SWITCH_ID)
        assertNotNull(switchItem)
        assertEquals(CatalogStatus.Ready, switchItem?.status)

        val radio = ComponentCatalog.findById(ComponentCatalog.RADIO_ID)
        assertNotNull(radio)
        assertEquals(CatalogStatus.Ready, radio?.status)

        val card = ComponentCatalog.findById(ComponentCatalog.CARD_ID)
        assertNotNull(card)
        assertEquals(CatalogStatus.Ready, card?.status)

        val topAppBar = ComponentCatalog.findById(ComponentCatalog.TOP_APP_BAR_ID)
        assertNotNull(topAppBar)
        assertEquals(CatalogStatus.Ready, topAppBar?.status)

        val bottomIsland = ComponentCatalog.findById(ComponentCatalog.BOTTOM_ISLAND_ID)
        assertNotNull(bottomIsland)
        assertEquals(CatalogStatus.Ready, bottomIsland?.status)

        val numberField = ComponentCatalog.findById(ComponentCatalog.NUMBER_FIELD_ID)
        assertNotNull(numberField)
        assertEquals(CatalogStatus.Ready, numberField?.status)

        val searchBar = ComponentCatalog.findById(ComponentCatalog.SEARCH_BAR_ID)
        assertNotNull(searchBar)
        assertEquals(CatalogStatus.Ready, searchBar?.status)

        val dateTimePicker = ComponentCatalog.findById(ComponentCatalog.DATE_TIME_PICKER_ID)
        assertNotNull(dateTimePicker)
        assertEquals(CatalogStatus.Ready, dateTimePicker?.status)

        val slider = ComponentCatalog.findById(ComponentCatalog.SLIDER_ID)
        assertNotNull(slider)
        assertEquals(CatalogStatus.Ready, slider?.status)

        val ratingBar = ComponentCatalog.findById(ComponentCatalog.RATING_BAR_ID)
        assertNotNull(ratingBar)
        assertEquals(CatalogStatus.Ready, ratingBar?.status)

        val dialog = ComponentCatalog.findById(ComponentCatalog.DIALOG_ID)
        assertNotNull(dialog)
        assertEquals(CatalogStatus.Ready, dialog?.status)

        val badge = ComponentCatalog.findById(ComponentCatalog.BADGE_ID)
        assertNotNull(badge)
        assertEquals(CatalogStatus.Ready, badge?.status)
    }

    @Test
    fun `every registered catalog item has Ready status`() {
        assertEquals(21, ComponentCatalog.items.size)
        assertEquals(
            setOf(CatalogStatus.Ready),
            ComponentCatalog.items.map { it.status }.toSet(),
        )
    }

    @Test
    fun `all five action button types are registered as ready under Actions`() {
        val actionItems = ComponentCatalog.items.filter { it.category == CatalogCategory.Actions }
        val actionIds = actionItems.map { it.id }.toSet()

        assertEquals(
            setOf("button", "icon-button", "fab", "segmented-button", "chip-button"),
            actionIds,
        )
        assertEquals(
            setOf(CatalogStatus.Ready),
            actionItems.map { it.status }.toSet(),
        )
    }
}
