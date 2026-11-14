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
    fun `color scheme and button items are registered as ready`() {
        val colorScheme = ComponentCatalog.findById(ComponentCatalog.COLOR_SCHEME_ID)
        assertNotNull(colorScheme)
        assertEquals(CatalogStatus.Ready, colorScheme?.status)

        val button = ComponentCatalog.findById(ComponentCatalog.BUTTON_ID)
        assertNotNull(button)
        assertEquals(CatalogStatus.Ready, button?.status)

        val textField = ComponentCatalog.findById(ComponentCatalog.TEXT_FIELD_ID)
        assertNotNull(textField)
        assertEquals(CatalogStatus.Ready, textField?.status)
    }

    @Test
    fun `all five action button types are registered under Actions`() {
        val actionIds = ComponentCatalog.items
            .filter { it.category == CatalogCategory.Actions }
            .map { it.id }
            .toSet()

        assertEquals(
            setOf("button", "icon-button", "fab", "segmented-button", "chip-button"),
            actionIds,
        )
    }
}
