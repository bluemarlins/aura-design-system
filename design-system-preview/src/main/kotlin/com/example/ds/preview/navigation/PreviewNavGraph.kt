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
import com.example.ds.preview.ui.PlannedComponentScreen

private object PreviewRoutes {
    const val HOME = "home"
    const val BUTTON = "component/${ComponentCatalog.BUTTON_ID}"
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
                    if (item.id == ComponentCatalog.BUTTON_ID) {
                        navController.navigate(PreviewRoutes.BUTTON)
                    } else {
                        navController.navigate(PreviewRoutes.planned(item.id))
                    }
                },
            )
        }
        composable(PreviewRoutes.BUTTON) {
            ButtonPreviewScreen(
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
