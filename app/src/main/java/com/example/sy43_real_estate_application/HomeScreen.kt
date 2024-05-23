package com.example.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    ScreenContent(navController = navController) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Welcome on RealEstateApp",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)

            )

            Button(
                onClick = { navController.navigate("listings") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("See listings")
            }
        }
    }
}
