package com.example.sy43_real_estate_application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun ScreenContent(navController: NavHostController, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController = navController)
        content()
    }
}
