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
    fun `button component is registered as ready`() {
        val button = ComponentCatalog.findById(ComponentCatalog.BUTTON_ID)
        assertNotNull(button)
        assertEquals(CatalogStatus.Ready, button?.status)
    }
}
