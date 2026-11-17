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
    const val INTERACTION_PRINCIPLES_ID = "interaction-principles"
    const val MATERIAL_COMPARISON_ID = "material-comparison"
    const val BUTTON_ID = "button"
    const val ICON_BUTTON_ID = "icon-button"
    const val FAB_ID = "fab"
    const val SEGMENTED_BUTTON_ID = "segmented-button"
    const val CHIP_BUTTON_ID = "chip-button"
    const val TEXT_FIELD_ID = "text-field"
    const val NUMBER_FIELD_ID = "number-field"
    const val SEARCH_BAR_ID = "search-bar"
    const val DATE_TIME_PICKER_ID = "date-time-picker"
    const val CHECKBOX_ID = "checkbox"
    const val SWITCH_ID = "switch"
    const val RADIO_ID = "radio"
    const val SLIDER_ID = "slider"
    const val RATING_BAR_ID = "rating-bar"
    const val TOP_APP_BAR_ID = "top-app-bar"
    const val BOTTOM_ISLAND_ID = "bottom-island"
    const val CARD_ID = "card"
    const val DIALOG_ID = "dialog"
    const val DIVIDER_ID = "divider"
    const val BADGE_ID = "badge"
    const val PROGRESS_BAR_ID = "progress-bar"
    const val SNACKBAR_ID = "snackbar"

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
            name = "AuraTypography, Shapes & Layout",
            category = CatalogCategory.Foundation,
            description = "Responsive typography scale, AuraShapes corner tokens, and 2-axis layout.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = INTERACTION_PRINCIPLES_ID,
            name = "Motion & Visual Interaction",
            category = CatalogCategory.Foundation,
            description = "Dual-track spring physics (Standard / Expressive), Solid/Glass/LiquidGlass materiality, and Z-axis elevation ladder.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = MATERIAL_COMPARISON_ID,
            name = "Material Comparison",
            category = CatalogCategory.Foundation,
            description = "Side-by-side Light / Dark specimens of Solid, Glass Morphism, and Liquid Glass with principles and fallbacks.",
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
            id = NUMBER_FIELD_ID,
            name = "AuraNumberField",
            category = CatalogCategory.Inputs,
            description = "Formatted numeric and currency input with unit affixes and steppers.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = SEARCH_BAR_ID,
            name = "AuraSearchBar",
            category = CatalogCategory.Inputs,
            description = "Search input with instant filter, badge counter, and Glass surface support.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = DATE_TIME_PICKER_ID,
            name = "AuraDateTimePicker",
            category = CatalogCategory.Inputs,
            description = "Date, time, and vintage year selector.",
            status = CatalogStatus.Ready,
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
            id = SLIDER_ID,
            name = "AuraSlider",
            category = CatalogCategory.Inputs,
            description = "Continuous and stepped value or sensory profile slider.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = RATING_BAR_ID,
            name = "AuraRatingBar",
            category = CatalogCategory.Inputs,
            description = "Star and half-star rating input for tasting notes.",
            status = CatalogStatus.Ready,
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
            id = DIALOG_ID,
            name = "AuraDialog",
            category = CatalogCategory.Containment,
            description = "Modal confirmation and alert surface with Standard, Destructive, and Glass modes.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = DIVIDER_ID,
            name = "AuraDivider",
            category = CatalogCategory.Containment,
            description = "Hairline horizontal and vertical separator using semantic border tokens.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = BADGE_ID,
            name = "AuraBadge",
            category = CatalogCategory.Feedback,
            description = "Compact count, status pill, and unread presence indicator.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = PROGRESS_BAR_ID,
            name = "AuraProgressBar",
            category = CatalogCategory.Feedback,
            description = "Linear and segmented step progress indicators with semantic status variants.",
            status = CatalogStatus.Ready,
        ),
        CatalogItem(
            id = SNACKBAR_ID,
            name = "AuraSnackbar",
            category = CatalogCategory.Feedback,
            description = "Queued transient feedback banner with Solid/Glass materiality and scaffold integration.",
            status = CatalogStatus.Ready,
        ),
    )

    fun findById(id: String): CatalogItem? = items.find { it.id == id }
}
