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
    const val TYPOGRAPHY_LAYOUT_ID = "typography-layout"
    const val BUTTON_ID = "button"
    const val ICON_BUTTON_ID = "icon-button"
    const val FAB_ID = "fab"
    const val SEGMENTED_BUTTON_ID = "segmented-button"
    const val CHIP_BUTTON_ID = "chip-button"
    const val TEXT_FIELD_ID = "text-field"
    const val CHECKBOX_ID = "checkbox"
    const val SWITCH_ID = "switch"
    const val RADIO_ID = "radio"
    const val TOP_APP_BAR_ID = "top-app-bar"
    const val BOTTOM_ISLAND_ID = "bottom-island"
    const val CARD_ID = "card"

    val items: List<CatalogItem> = listOf(
        CatalogItem(
            id = COLOR_SCHEME_ID,
            name = "AuraColors",
            category = CatalogCategory.Foundation,
            description = "One UI-inspired Light & Dark color scheme with floating surface layer.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = TYPOGRAPHY_LAYOUT_ID,
            name = "AuraTypography & Layout",
            category = CatalogCategory.Foundation,
            description = "Responsive typography scale and 2-axis layout (Smallest Width × Display Ratio).",
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
            id = ICON_BUTTON_ID,
            name = "AuraIconButton",
            category = CatalogCategory.Actions,
            description = "Compact icon-only action control.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = FAB_ID,
            name = "AuraFab",
            category = CatalogCategory.Actions,
            description = "Persistent floating action button for primary screen creation.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = SEGMENTED_BUTTON_ID,
            name = "AuraSegmentedButton",
            category = CatalogCategory.Actions,
            description = "Immediate mutually exclusive view mode and unit switcher.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = CHIP_BUTTON_ID,
            name = "AuraChipButton",
            category = CatalogCategory.Actions,
            description = "Compact capsule toggle for filters and attribute tags.",
            status = CatalogStatus.Ready,
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
            id = CHECKBOX_ID,
            name = "AuraCheckbox",
            category = CatalogCategory.Inputs,
            description = "Binary selection control.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = SWITCH_ID,
            name = "AuraSwitch",
            category = CatalogCategory.Inputs,
            description = "Instant boolean toggle control.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = RADIO_ID,
            name = "AuraRadio",
            category = CatalogCategory.Inputs,
            description = "Mutually exclusive single-choice control.",
            status = CatalogStatus.Ready,
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
            id = TOP_APP_BAR_ID,
            name = "AuraTopAppBar · Floating Pill",
            category = CatalogCategory.Navigation,
            description = "Scroll-adaptive Floating Pill Bar with Dual Glass & Morphing Popover.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = BOTTOM_ISLAND_ID,
            name = "AuraBottomIsland · Split Island",
            category = CatalogCategory.Navigation,
            description = "Floating Bottom Island with Metaball split action & elastic sliding pill.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = CARD_ID,
            name = "AuraCard",
            category = CatalogCategory.Containment,
            description = "Grouped surface container for related content.",
            status = CatalogStatus.Ready,
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
