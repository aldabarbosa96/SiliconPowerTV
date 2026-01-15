package com.davidbarbosa.siliconpowertv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.davidbarbosa.siliconpowertv.ui.detail.DetailScreen
import com.davidbarbosa.siliconpowertv.ui.list.PopularScreen
import com.davidbarbosa.siliconpowertv.ui.theme.SiliconPowerTVTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiliconPowerTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController, startDestination = "popular"
                    ) {
                        composable("popular") {
                            PopularScreen(onItemClick = { id ->
                                navController.navigate("detail/$id")
                            })
                        }

                        composable("detail/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                                ?: return@composable

                            DetailScreen(tvId = id, onItemClick = { recId ->
                                navController.navigate("detail/$recId")
                            })
                        }
                    }
                }
            }
        }
    }
}
