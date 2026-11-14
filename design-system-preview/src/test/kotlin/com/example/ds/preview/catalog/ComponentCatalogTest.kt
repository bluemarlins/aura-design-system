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
