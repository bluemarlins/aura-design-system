package com.example.ds.preview.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ds.preview.catalog.ComponentCatalog
import com.example.ds.preview.ui.ButtonPreviewScreen
import com.example.ds.preview.ui.CatalogHomeScreen
import com.example.ds.preview.ui.ColorSchemePreviewScreen
import com.example.ds.preview.ui.PlannedComponentScreen
import com.example.ds.preview.ui.TextFieldPreviewScreen

private object PreviewRoutes {
    const val HOME = "home"
    const val COLOR_SCHEME = "foundation/${ComponentCatalog.COLOR_SCHEME_ID}"
    const val BUTTON = "component/${ComponentCatalog.BUTTON_ID}"
    const val TEXT_FIELD = "component/${ComponentCatalog.TEXT_FIELD_ID}"
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
                        ComponentCatalog.BUTTON_ID -> navController.navigate(PreviewRoutes.BUTTON)
                        ComponentCatalog.TEXT_FIELD_ID -> navController.navigate(PreviewRoutes.TEXT_FIELD)
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
        composable(PreviewRoutes.BUTTON) {
            ButtonPreviewScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable(PreviewRoutes.TEXT_FIELD) {
            TextFieldPreviewScreen(
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
