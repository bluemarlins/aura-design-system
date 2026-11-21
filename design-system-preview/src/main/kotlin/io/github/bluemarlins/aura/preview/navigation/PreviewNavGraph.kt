package io.github.bluemarlins.aura.preview.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProfile
import io.github.bluemarlins.aura.foundation.theme.AuraMaterial
import io.github.bluemarlins.aura.preview.catalog.CatalogCategory
import io.github.bluemarlins.aura.preview.catalog.ComponentCatalog
import io.github.bluemarlins.aura.preview.ui.ButtonPreviewScreen
import io.github.bluemarlins.aura.preview.ui.CardPreviewScreen
import io.github.bluemarlins.aura.preview.ui.CatalogHomeScreen
import io.github.bluemarlins.aura.preview.ui.CheckboxPreviewScreen
import io.github.bluemarlins.aura.preview.ui.ChipButtonPreviewScreen
import io.github.bluemarlins.aura.preview.ui.ColorSchemePreviewScreen
import io.github.bluemarlins.aura.preview.ui.DateTimePickerPreviewScreen
import io.github.bluemarlins.aura.preview.ui.DialogBadgePreviewScreen
import io.github.bluemarlins.aura.preview.ui.DividerPreviewScreen
import io.github.bluemarlins.aura.preview.ui.FabPreviewScreen
import io.github.bluemarlins.aura.preview.ui.FeedbackProgressSnackbarPreviewScreen
import io.github.bluemarlins.aura.preview.ui.FloatingNavigationPreviewScreen
import io.github.bluemarlins.aura.preview.ui.IconButtonPreviewScreen
import io.github.bluemarlins.aura.preview.ui.InteractionPrinciplesPreviewScreen
import io.github.bluemarlins.aura.preview.ui.MaterialComparisonPreviewScreen
import io.github.bluemarlins.aura.preview.ui.NumberFieldPreviewScreen
import io.github.bluemarlins.aura.preview.ui.PlannedComponentScreen
import io.github.bluemarlins.aura.preview.ui.RadioPreviewScreen
import io.github.bluemarlins.aura.preview.ui.SearchBarPreviewScreen
import io.github.bluemarlins.aura.preview.ui.SegmentedButtonPreviewScreen
import io.github.bluemarlins.aura.preview.ui.SliderRatingPreviewScreen
import io.github.bluemarlins.aura.preview.ui.SwitchPreviewScreen
import io.github.bluemarlins.aura.preview.ui.TextFieldPreviewScreen
import io.github.bluemarlins.aura.preview.ui.TypographyLayoutPreviewScreen
import io.github.bluemarlins.aura.preview.ui.showcase.ShowcaseGroupScreen
import io.github.bluemarlins.aura.preview.ui.showcase.ShowcaseGroups

private object PreviewRoutes {
    const val HOME = "home"
    const val COLOR_SCHEME = "foundation/${ComponentCatalog.COLOR_SCHEME_ID}"
    const val TYPOGRAPHY_LAYOUT = "foundation/${ComponentCatalog.TYPOGRAPHY_LAYOUT_ID}"
    const val INTERACTION_PRINCIPLES = "foundation/${ComponentCatalog.INTERACTION_PRINCIPLES_ID}"
    const val MATERIAL_COMPARISON = "foundation/${ComponentCatalog.MATERIAL_COMPARISON_ID}"
    const val BUTTON = "component/${ComponentCatalog.BUTTON_ID}"
    const val ICON_BUTTON = "component/${ComponentCatalog.ICON_BUTTON_ID}"
    const val FAB = "component/${ComponentCatalog.FAB_ID}"
    const val SEGMENTED_BUTTON = "component/${ComponentCatalog.SEGMENTED_BUTTON_ID}"
    const val CHIP_BUTTON = "component/${ComponentCatalog.CHIP_BUTTON_ID}"
    const val TEXT_FIELD = "component/${ComponentCatalog.TEXT_FIELD_ID}"
    const val NUMBER_FIELD = "component/${ComponentCatalog.NUMBER_FIELD_ID}"
    const val SEARCH_BAR = "component/${ComponentCatalog.SEARCH_BAR_ID}"
    const val DATE_TIME_PICKER = "component/${ComponentCatalog.DATE_TIME_PICKER_ID}"
    const val CHECKBOX = "component/${ComponentCatalog.CHECKBOX_ID}"
    const val SWITCH = "component/${ComponentCatalog.SWITCH_ID}"
    const val RADIO = "component/${ComponentCatalog.RADIO_ID}"
    const val SLIDER = "component/${ComponentCatalog.SLIDER_ID}"
    const val RATING_BAR = "component/${ComponentCatalog.RATING_BAR_ID}"
    const val TOP_APP_BAR = "component/${ComponentCatalog.TOP_APP_BAR_ID}"
    const val BOTTOM_ISLAND = "component/${ComponentCatalog.BOTTOM_ISLAND_ID}"
    const val CARD = "component/${ComponentCatalog.CARD_ID}"
    const val DIALOG = "component/${ComponentCatalog.DIALOG_ID}"
    const val DIVIDER = "component/${ComponentCatalog.DIVIDER_ID}"
    const val BADGE = "component/${ComponentCatalog.BADGE_ID}"
    const val PROGRESS_BAR = "component/${ComponentCatalog.PROGRESS_BAR_ID}"
    const val SNACKBAR = "component/${ComponentCatalog.SNACKBAR_ID}"
    const val PLANNED = "planned/{itemId}"
    const val SHOWCASE = "showcase/{groupId}"

    fun planned(itemId: String): String = "planned/$itemId"
    fun showcase(groupId: String): String = "showcase/$groupId"

    /** Routes for catalog ids that open a component or foundation screen directly. */
    fun forCatalogId(id: String): String? = when (id) {
        ComponentCatalog.COLOR_SCHEME_ID -> COLOR_SCHEME
        ComponentCatalog.TYPOGRAPHY_LAYOUT_ID -> TYPOGRAPHY_LAYOUT
        ComponentCatalog.INTERACTION_PRINCIPLES_ID -> INTERACTION_PRINCIPLES
        ComponentCatalog.MATERIAL_COMPARISON_ID -> MATERIAL_COMPARISON
        ComponentCatalog.BUTTON_ID -> BUTTON
        ComponentCatalog.ICON_BUTTON_ID -> ICON_BUTTON
        ComponentCatalog.FAB_ID -> FAB
        ComponentCatalog.SEGMENTED_BUTTON_ID -> SEGMENTED_BUTTON
        ComponentCatalog.CHIP_BUTTON_ID -> CHIP_BUTTON
        ComponentCatalog.TEXT_FIELD_ID -> TEXT_FIELD
        ComponentCatalog.NUMBER_FIELD_ID -> NUMBER_FIELD
        ComponentCatalog.SEARCH_BAR_ID -> SEARCH_BAR
        ComponentCatalog.DATE_TIME_PICKER_ID -> DATE_TIME_PICKER
        ComponentCatalog.CHECKBOX_ID -> CHECKBOX
        ComponentCatalog.SWITCH_ID -> SWITCH
        ComponentCatalog.RADIO_ID -> RADIO
        ComponentCatalog.SLIDER_ID -> SLIDER
        ComponentCatalog.RATING_BAR_ID -> RATING_BAR
        ComponentCatalog.TOP_APP_BAR_ID -> TOP_APP_BAR
        ComponentCatalog.BOTTOM_ISLAND_ID -> BOTTOM_ISLAND
        ComponentCatalog.CARD_ID -> CARD
        ComponentCatalog.DIALOG_ID -> DIALOG
        ComponentCatalog.DIVIDER_ID -> DIVIDER
        ComponentCatalog.BADGE_ID -> BADGE
        ComponentCatalog.PROGRESS_BAR_ID -> PROGRESS_BAR
        ComponentCatalog.SNACKBAR_ID -> SNACKBAR
        else -> null
    }
}

@Composable
fun PreviewNavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    motionProfile: AuraMotionProfile = AuraMotionProfile.Standard,
    onSelectMotionProfile: (AuraMotionProfile) -> Unit = {},
    material: AuraMaterial = AuraMaterial.Solid,
    onSelectMaterial: (AuraMaterial) -> Unit = {},
    reduceMotion: Boolean = false,
    onToggleReduceMotion: (Boolean) -> Unit = {},
    reduceTransparency: Boolean = false,
    onToggleReduceTransparency: (Boolean) -> Unit = {},
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PreviewRoutes.HOME,
    ) {
        composable(PreviewRoutes.HOME) {
            CatalogHomeScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                motionProfile = motionProfile,
                onSelectMotionProfile = onSelectMotionProfile,
                material = material,
                onSelectMaterial = onSelectMaterial,
                reduceMotion = reduceMotion,
                onToggleReduceMotion = onToggleReduceMotion,
                reduceTransparency = reduceTransparency,
                onToggleReduceTransparency = onToggleReduceTransparency,
                onSelectItem = { item ->
                    val route = if (item.category == CatalogCategory.Showcase) {
                        PreviewRoutes.showcase(item.id)
                    } else {
                        PreviewRoutes.forCatalogId(item.id) ?: PreviewRoutes.planned(item.id)
                    }
                    navController.navigate(route)
                },
            )
        }
        composable(PreviewRoutes.COLOR_SCHEME) {
            ColorSchemePreviewScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.TYPOGRAPHY_LAYOUT) {
            TypographyLayoutPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.INTERACTION_PRINCIPLES) {
            InteractionPrinciplesPreviewScreen(
                onBack = { navController.popBackStack() },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                initialProfile = motionProfile,
                onProfileChange = onSelectMotionProfile,
                initialMaterial = material,
                onMaterialChange = onSelectMaterial,
                initialReduceMotion = reduceMotion,
                onReduceMotionChange = onToggleReduceMotion,
                initialReduceTransparency = reduceTransparency,
                onReduceTransparencyChange = onToggleReduceTransparency,
            )
        }
        composable(PreviewRoutes.MATERIAL_COMPARISON) {
            MaterialComparisonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.BUTTON) {
            ButtonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.ICON_BUTTON) {
            IconButtonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.FAB) {
            FabPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.SEGMENTED_BUTTON) {
            SegmentedButtonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.CHIP_BUTTON) {
            ChipButtonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.TEXT_FIELD) {
            TextFieldPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.NUMBER_FIELD) {
            NumberFieldPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.SEARCH_BAR) {
            SearchBarPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.DATE_TIME_PICKER) {
            DateTimePickerPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.CHECKBOX) {
            CheckboxPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.SWITCH) {
            SwitchPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.RADIO) {
            RadioPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.SLIDER) {
            SliderRatingPreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusRating = false,
            )
        }
        composable(PreviewRoutes.RATING_BAR) {
            SliderRatingPreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusRating = true,
            )
        }
        composable(PreviewRoutes.CARD) {
            CardPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.DIALOG) {
            DialogBadgePreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusBadge = false,
            )
        }
        composable(PreviewRoutes.DIVIDER) {
            DividerPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.BADGE) {
            DialogBadgePreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusBadge = true,
            )
        }
        composable(PreviewRoutes.PROGRESS_BAR) {
            FeedbackProgressSnackbarPreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusSnackbar = false,
            )
        }
        composable(PreviewRoutes.SNACKBAR) {
            FeedbackProgressSnackbarPreviewScreen(
                onBack = { navController.popBackStack() },
                initialFocusSnackbar = true,
            )
        }
        composable(PreviewRoutes.TOP_APP_BAR) {
            FloatingNavigationPreviewScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
                initialTabIndex = 0,
            )
        }
        composable(PreviewRoutes.BOTTOM_ISLAND) {
            FloatingNavigationPreviewScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
                initialTabIndex = 1,
            )
        }
        composable(
            route = PreviewRoutes.SHOWCASE,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId").orEmpty()
            val group = ShowcaseGroups.byId(groupId)
            if (group == null) {
                PlannedComponentScreen(
                    item = ComponentCatalog.findById(groupId),
                    onBack = { navController.popBackStack() },
                )
            } else {
                ShowcaseGroupScreen(
                    group = group,
                    onBack = { navController.popBackStack() },
                    onOpenComponent = { catalogId ->
                        PreviewRoutes.forCatalogId(catalogId)?.let { navController.navigate(it) }
                    },
                )
            }
        }
        composable(
            route = PreviewRoutes.PLANNED,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId").orEmpty()
            PlannedComponentScreen(
                item = ComponentCatalog.findById(itemId),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
