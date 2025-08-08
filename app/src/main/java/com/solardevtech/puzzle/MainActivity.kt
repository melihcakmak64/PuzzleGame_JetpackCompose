package com.solardevtech.puzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.solardevtech.puzzle.ui.theme.PuzzleTheme
import com.solardevtech.puzzle.view.screens.DragAndDropWithGridScreen
import com.solardevtech.puzzle.view.screens.ImageListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ImageListScreen(
                            onImageSelected = { imageUrl ->
                                val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
                                navController.navigate("puzzle/$encodedUrl")
                            }
                        )
                    }

                    composable(
                        route = "puzzle/{imageUrl}",
                        arguments = listOf(navArgument("imageUrl") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                        DragAndDropWithGridScreen(imageUrl = java.net.URLDecoder.decode(imageUrl, "UTF-8"))
                    }
                }
            }
        }
    }
}
