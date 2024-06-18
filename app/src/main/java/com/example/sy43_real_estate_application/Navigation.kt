package com.example.sy43_real_estate_application

import UserViewModel
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, userViewModel) }
        composable("login") { LoginScreen(navController, userViewModel, context) }
        composable("listings") { ListingsScreen(navController, userViewModel) }
        composable("register") { RegisterScreen(navController, userViewModel, context) }
        composable("wishlist") { WishlistScreen(navController, userViewModel, context) }
    }
}
