package com.example.ds.preview.catalog

enum class CatalogCategory(val label: String) {
    Actions("Actions & Controls"),
    Inputs("Data Input & Selection"),
    Navigation("Navigation & Hierarchy"),
    Containment("Containment & Surfaces"),
    Feedback("Feedback & Progress"),
}

enum class CatalogStatus(val label: String) {
    Ready("Ready"),
    Planned("Planned"),
}

data class CatalogItem(
    val id: String,
    val name: String,
    val category: CatalogCategory,
    val description: String,
    val status: CatalogStatus,
)

object ComponentCatalog {
    const val BUTTON_ID = "button"

    val items: List<CatalogItem> = listOf(
        CatalogItem(
            id = BUTTON_ID,
            name = "DsButton",
            category = CatalogCategory.Actions,
            description = "Simple contained button for primary actions.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = "icon-button",
            name = "DsIconButton",
            category = CatalogCategory.Actions,
            description = "Compact icon-only action control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "text-field",
            name = "DsTextField",
            category = CatalogCategory.Inputs,
            description = "Single-line and multi-line text input.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "checkbox",
            name = "DsCheckbox",
            category = CatalogCategory.Inputs,
            description = "Binary selection control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "switch",
            name = "DsSwitch",
            category = CatalogCategory.Inputs,
            description = "Instant boolean toggle control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "top-app-bar",
            name = "DsTopAppBar",
            category = CatalogCategory.Navigation,
            description = "Screen header with navigation and actions.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "card",
            name = "DsCard",
            category = CatalogCategory.Containment,
            description = "Grouped surface container for related content.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "dialog",
            name = "DsDialog",
            category = CatalogCategory.Containment,
            description = "Modal confirmation and alert surface.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "badge",
            name = "DsBadge",
            category = CatalogCategory.Feedback,
            description = "Compact count and status indicator.",
            status = CatalogStatus.Planned,
        ),
    )

    fun findById(id: String): CatalogItem? = items.find { it.id == id }
}
