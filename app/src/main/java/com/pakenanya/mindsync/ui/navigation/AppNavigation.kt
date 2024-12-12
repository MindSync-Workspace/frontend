package com.pakenanya.mindsync.ui.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pakenanya.mindsync.ui.screen.main.MainScreen
import androidx.navigation.compose.rememberNavController
import com.pakenanya.mindsync.ui.screen.auth.AuthViewModel
import com.pakenanya.mindsync.ui.animation.PageAnimation.slideOutToBottom
import com.pakenanya.mindsync.ui.animation.PageAnimation.slideInFromBottom
import com.pakenanya.mindsync.ui.screen.auth.LoginScreen
import com.pakenanya.mindsync.ui.screen.auth.RegisterScreen
import com.pakenanya.mindsync.ui.screen.main.document.DocumentDetailScreen
import com.pakenanya.mindsync.ui.screen.main.note.NoteDetailScreen
import com.pakenanya.mindsync.ui.screen.profile.EditProfileScreen
import com.pakenanya.mindsync.ui.screen.profile.ProfileScreen
import com.pakenanya.mindsync.ui.screen.profile.WhatsappBindScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    initialToken: String? = null
) {
    val navController = rememberNavController()

    val startDestination = if (initialToken.isNullOrEmpty()) {
        Routes.LOGIN
    } else {
        Routes.MAIN_SCREEN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            Routes.LOGIN,
            enterTransition = { slideInFromBottom },
            exitTransition = { slideOutToBottom }
        ) {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(modifier, navController, authViewModel)
        }
        composable(Routes.MAIN_SCREEN) {
            MainScreen(modifier, navController, authViewModel)
        }
        composable(Routes.NOTE_DETAIL) { backStackEntry ->
            NoteDetailScreen(modifier, navController, backStackEntry)
        }
        composable(Routes.DOCUMENT_DETAIL) { backStackEntry ->
            DocumentDetailScreen(modifier, navController, backStackEntry)
        }
        composable(Routes.PROFILE) {
            ProfileScreen(modifier, navController, authViewModel)
        }
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(modifier, navController, authViewModel)
        }
        composable(Routes.CONNECT_BOT) {
            WhatsappBindScreen(modifier, navController, authViewModel)
        }
    }
}