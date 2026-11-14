package com.example.ds.preview.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ds.preview.catalog.ComponentCatalog
import com.example.ds.preview.ui.ButtonPreviewScreen
import com.example.ds.preview.ui.CardPreviewScreen
import com.example.ds.preview.ui.CatalogHomeScreen
import com.example.ds.preview.ui.CheckboxPreviewScreen
import com.example.ds.preview.ui.ChipButtonPreviewScreen
import com.example.ds.preview.ui.ColorSchemePreviewScreen
import com.example.ds.preview.ui.FabPreviewScreen
import com.example.ds.preview.ui.IconButtonPreviewScreen
import com.example.ds.preview.ui.PlannedComponentScreen
import com.example.ds.preview.ui.RadioPreviewScreen
import com.example.ds.preview.ui.SegmentedButtonPreviewScreen
import com.example.ds.preview.ui.SwitchPreviewScreen
import com.example.ds.preview.ui.TextFieldPreviewScreen
import com.example.ds.preview.ui.TypographyLayoutPreviewScreen

private object PreviewRoutes {
    const val HOME = "home"
    const val COLOR_SCHEME = "foundation/${ComponentCatalog.COLOR_SCHEME_ID}"
    const val TYPOGRAPHY_LAYOUT = "foundation/${ComponentCatalog.TYPOGRAPHY_LAYOUT_ID}"
    const val BUTTON = "component/${ComponentCatalog.BUTTON_ID}"
    const val ICON_BUTTON = "component/${ComponentCatalog.ICON_BUTTON_ID}"
    const val FAB = "component/${ComponentCatalog.FAB_ID}"
    const val SEGMENTED_BUTTON = "component/${ComponentCatalog.SEGMENTED_BUTTON_ID}"
    const val CHIP_BUTTON = "component/${ComponentCatalog.CHIP_BUTTON_ID}"
    const val TEXT_FIELD = "component/${ComponentCatalog.TEXT_FIELD_ID}"
    const val CHECKBOX = "component/${ComponentCatalog.CHECKBOX_ID}"
    const val SWITCH = "component/${ComponentCatalog.SWITCH_ID}"
    const val RADIO = "component/${ComponentCatalog.RADIO_ID}"
    const val CARD = "component/${ComponentCatalog.CARD_ID}"
    const val PLANNED = "planned/{itemId}"

    fun planned(itemId: String): String = "planned/$itemId"
}

@Composable
fun PreviewNavGraph(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
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
                onSelectItem = { item ->
                    when (item.id) {
                        ComponentCatalog.COLOR_SCHEME_ID -> navController.navigate(PreviewRoutes.COLOR_SCHEME)
                        ComponentCatalog.TYPOGRAPHY_LAYOUT_ID -> navController.navigate(PreviewRoutes.TYPOGRAPHY_LAYOUT)
                        ComponentCatalog.BUTTON_ID -> navController.navigate(PreviewRoutes.BUTTON)
                        ComponentCatalog.ICON_BUTTON_ID -> navController.navigate(PreviewRoutes.ICON_BUTTON)
                        ComponentCatalog.FAB_ID -> navController.navigate(PreviewRoutes.FAB)
                        ComponentCatalog.SEGMENTED_BUTTON_ID -> navController.navigate(PreviewRoutes.SEGMENTED_BUTTON)
                        ComponentCatalog.CHIP_BUTTON_ID -> navController.navigate(PreviewRoutes.CHIP_BUTTON)
                        ComponentCatalog.TEXT_FIELD_ID -> navController.navigate(PreviewRoutes.TEXT_FIELD)
                        ComponentCatalog.CHECKBOX_ID -> navController.navigate(PreviewRoutes.CHECKBOX)
                        ComponentCatalog.SWITCH_ID -> navController.navigate(PreviewRoutes.SWITCH)
                        ComponentCatalog.RADIO_ID -> navController.navigate(PreviewRoutes.RADIO)
                        ComponentCatalog.CARD_ID -> navController.navigate(PreviewRoutes.CARD)
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
        composable(PreviewRoutes.CARD) {
            CardPreviewScreen(
                onBack = { navController.popBackStack() },
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
