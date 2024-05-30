package com.example.sy43_real_estate_application

import UserViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenContent(navController: NavHostController, userViewModel: UserViewModel, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            content()
            Spacer(modifier = Modifier.weight(1f))
        }
        TopBar(
            navController = navController,
            userViewModel = userViewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}
