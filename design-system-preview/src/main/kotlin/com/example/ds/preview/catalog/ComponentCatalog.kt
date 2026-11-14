package com.example.ds.preview.catalog

enum class CatalogCategory(val label: String) {
    Foundation("Foundation & Tokens"),
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
    const val COLOR_SCHEME_ID = "color-scheme"
    const val BUTTON_ID = "button"
    const val TEXT_FIELD_ID = "text-field"

    val items: List<CatalogItem> = listOf(
        CatalogItem(
            id = COLOR_SCHEME_ID,
            name = "AuraColors",
            category = CatalogCategory.Foundation,
            description = "One UI-inspired Light & Dark color scheme with floating surface layer.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = BUTTON_ID,
            name = "AuraButton",
            category = CatalogCategory.Actions,
            description = "Labeled action button with variants, sizes, loading, and debounce.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = "icon-button",
            name = "AuraIconButton",
            category = CatalogCategory.Actions,
            description = "Compact icon-only action control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "fab",
            name = "AuraFab",
            category = CatalogCategory.Actions,
            description = "Persistent floating action button for primary screen creation.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "segmented-button",
            name = "AuraSegmentedButton",
            category = CatalogCategory.Actions,
            description = "Immediate mutually exclusive view mode and unit switcher.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "chip-button",
            name = "AuraChipButton",
            category = CatalogCategory.Actions,
            description = "Compact capsule toggle for filters and attribute tags.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = TEXT_FIELD_ID,
            name = "AuraTextField",
            category = CatalogCategory.Inputs,
            description = "Single-line and multi-line text input with Outlined, Filled, and Paperless variants.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = "number-field",
            name = "AuraNumberField",
            category = CatalogCategory.Inputs,
            description = "Formatted numeric and currency input with unit affixes.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "search-bar",
            name = "AuraSearchBar",
            category = CatalogCategory.Inputs,
            description = "Search input with instant filter and Glass surface support.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "date-time-picker",
            name = "AuraDateTimePicker",
            category = CatalogCategory.Inputs,
            description = "Date, time, vintage year, and period selector.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "checkbox",
            name = "AuraCheckbox",
            category = CatalogCategory.Inputs,
            description = "Binary selection control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "switch",
            name = "AuraSwitch",
            category = CatalogCategory.Inputs,
            description = "Instant boolean toggle control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "radio",
            name = "AuraRadio",
            category = CatalogCategory.Inputs,
            description = "Mutually exclusive single-choice control.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "slider",
            name = "AuraSlider",
            category = CatalogCategory.Inputs,
            description = "Continuous and stepped value or sensory profile slider.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "rating-bar",
            name = "AuraRatingBar",
            category = CatalogCategory.Inputs,
            description = "Star and half-star rating input for tasting notes.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "top-app-bar",
            name = "AuraTopAppBar",
            category = CatalogCategory.Navigation,
            description = "Screen header with navigation and actions.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "card",
            name = "AuraCard",
            category = CatalogCategory.Containment,
            description = "Grouped surface container for related content.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "dialog",
            name = "AuraDialog",
            category = CatalogCategory.Containment,
            description = "Modal confirmation and alert surface.",
            status = CatalogStatus.Planned,
        ),
        CatalogItem(
            id = "badge",
            name = "AuraBadge",
            category = CatalogCategory.Feedback,
            description = "Compact count and status indicator.",
            status = CatalogStatus.Planned,
        ),
    )

    fun findById(id: String): CatalogItem? = items.find { it.id == id }
}
