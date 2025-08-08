package com.solardevtech.puzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.solardevtech.puzzle.model.ImageItem
import com.solardevtech.puzzle.ui.theme.PuzzleTheme
import com.solardevtech.puzzle.view.screens.DragAndDropWithGridScreen
import com.solardevtech.puzzle.view.screens.ImageListScreen
import com.solardevtech.puzzle.view.screens.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(
                            onSuccess = { images ->
                                val key = "shared_images"
                                // Listeyi JSON stringe çevirip kaydet
                                val json = gson.toJson(images)
                                navController.currentBackStackEntry?.savedStateHandle?.set(key, json)
                                navController.navigate("list")
                            }
                        )
                    }

                    composable("list") {
                        val key = "shared_images"
                        val json = navController.previousBackStackEntry?.savedStateHandle?.get<String>(key)
                        val images = json?.let {
                            val type = object : TypeToken<List<ImageItem>>() {}.type
                            gson.fromJson<List<ImageItem>>(it, type)
                        }
                        images?.let {
                            ImageListScreen(
                                images = it,
                                onImageSelected = { imageUrl ->
                                    val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
                                    navController.navigate("puzzle/$encodedUrl")
                                }
                            )
                        }
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
