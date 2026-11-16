package com.example.ds.preview.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ds.foundation.motion.AuraMotionProfile
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.preview.catalog.ComponentCatalog
import com.example.ds.preview.ui.ButtonPreviewScreen
import com.example.ds.preview.ui.CardPreviewScreen
import com.example.ds.preview.ui.CatalogHomeScreen
import com.example.ds.preview.ui.CheckboxPreviewScreen
import com.example.ds.preview.ui.ChipButtonPreviewScreen
import com.example.ds.preview.ui.ColorSchemePreviewScreen
import com.example.ds.preview.ui.DateTimePickerPreviewScreen
import com.example.ds.preview.ui.DialogBadgePreviewScreen
import com.example.ds.preview.ui.DividerPreviewScreen
import com.example.ds.preview.ui.FabPreviewScreen
import com.example.ds.preview.ui.FeedbackProgressSnackbarPreviewScreen
import com.example.ds.preview.ui.FloatingNavigationPreviewScreen
import com.example.ds.preview.ui.IconButtonPreviewScreen
import com.example.ds.preview.ui.InteractionPrinciplesPreviewScreen
import com.example.ds.preview.ui.NumberFieldPreviewScreen
import com.example.ds.preview.ui.PlannedComponentScreen
import com.example.ds.preview.ui.RadioPreviewScreen
import com.example.ds.preview.ui.SearchBarPreviewScreen
import com.example.ds.preview.ui.SegmentedButtonPreviewScreen
import com.example.ds.preview.ui.SliderRatingPreviewScreen
import com.example.ds.preview.ui.SwitchPreviewScreen
import com.example.ds.preview.ui.TextFieldPreviewScreen
import com.example.ds.preview.ui.TypographyLayoutPreviewScreen

private object PreviewRoutes {
    const val HOME = "home"
    const val COLOR_SCHEME = "foundation/${ComponentCatalog.COLOR_SCHEME_ID}"
    const val TYPOGRAPHY_LAYOUT = "foundation/${ComponentCatalog.TYPOGRAPHY_LAYOUT_ID}"
    const val INTERACTION_PRINCIPLES = "foundation/${ComponentCatalog.INTERACTION_PRINCIPLES_ID}"
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

    fun planned(itemId: String): String = "planned/$itemId"
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
                    when (item.id) {
                        ComponentCatalog.COLOR_SCHEME_ID -> navController.navigate(PreviewRoutes.COLOR_SCHEME)
                        ComponentCatalog.TYPOGRAPHY_LAYOUT_ID -> navController.navigate(PreviewRoutes.TYPOGRAPHY_LAYOUT)
                        ComponentCatalog.INTERACTION_PRINCIPLES_ID -> navController.navigate(PreviewRoutes.INTERACTION_PRINCIPLES)
                        ComponentCatalog.BUTTON_ID -> navController.navigate(PreviewRoutes.BUTTON)
                        ComponentCatalog.ICON_BUTTON_ID -> navController.navigate(PreviewRoutes.ICON_BUTTON)
                        ComponentCatalog.FAB_ID -> navController.navigate(PreviewRoutes.FAB)
                        ComponentCatalog.SEGMENTED_BUTTON_ID -> navController.navigate(PreviewRoutes.SEGMENTED_BUTTON)
                        ComponentCatalog.CHIP_BUTTON_ID -> navController.navigate(PreviewRoutes.CHIP_BUTTON)
                        ComponentCatalog.TEXT_FIELD_ID -> navController.navigate(PreviewRoutes.TEXT_FIELD)
                        ComponentCatalog.NUMBER_FIELD_ID -> navController.navigate(PreviewRoutes.NUMBER_FIELD)
                        ComponentCatalog.SEARCH_BAR_ID -> navController.navigate(PreviewRoutes.SEARCH_BAR)
                        ComponentCatalog.DATE_TIME_PICKER_ID -> navController.navigate(PreviewRoutes.DATE_TIME_PICKER)
                        ComponentCatalog.CHECKBOX_ID -> navController.navigate(PreviewRoutes.CHECKBOX)
                        ComponentCatalog.SWITCH_ID -> navController.navigate(PreviewRoutes.SWITCH)
                        ComponentCatalog.RADIO_ID -> navController.navigate(PreviewRoutes.RADIO)
                        ComponentCatalog.SLIDER_ID -> navController.navigate(PreviewRoutes.SLIDER)
                        ComponentCatalog.RATING_BAR_ID -> navController.navigate(PreviewRoutes.RATING_BAR)
                        ComponentCatalog.TOP_APP_BAR_ID -> navController.navigate(PreviewRoutes.TOP_APP_BAR)
                        ComponentCatalog.BOTTOM_ISLAND_ID -> navController.navigate(PreviewRoutes.BOTTOM_ISLAND)
                        ComponentCatalog.CARD_ID -> navController.navigate(PreviewRoutes.CARD)
                        ComponentCatalog.DIALOG_ID -> navController.navigate(PreviewRoutes.DIALOG)
                        ComponentCatalog.DIVIDER_ID -> navController.navigate(PreviewRoutes.DIVIDER)
                        ComponentCatalog.BADGE_ID -> navController.navigate(PreviewRoutes.BADGE)
                        ComponentCatalog.PROGRESS_BAR_ID -> navController.navigate(PreviewRoutes.PROGRESS_BAR)
                        ComponentCatalog.SNACKBAR_ID -> navController.navigate(PreviewRoutes.SNACKBAR)
                        else -> navController.navigate(PreviewRoutes.planned(item.id))
                    }
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
