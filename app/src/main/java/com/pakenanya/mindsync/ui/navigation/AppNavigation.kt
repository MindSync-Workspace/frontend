package com.pakenanya.mindsync.ui.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pakenanya.mindsync.ui.screen.auth.AuthScreen
import com.pakenanya.mindsync.ui.screen.main.MainScreen
import androidx.navigation.compose.rememberNavController
import com.pakenanya.mindsync.ui.animation.PageAnimation.slideOutToBottom
import com.pakenanya.mindsync.ui.animation.PageAnimation.slideInFromBottom

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH,
        modifier = modifier
    ) {
        composable(
            Routes.AUTH,
            enterTransition = { slideInFromBottom },
            exitTransition = { slideOutToBottom }
        ) {
            AuthScreen {
                navController.navigate(Routes.MAIN_SCREEN) {
                    popUpTo(Routes.AUTH) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Routes.MAIN_SCREEN) {
            MainScreen(
                onLogout = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}