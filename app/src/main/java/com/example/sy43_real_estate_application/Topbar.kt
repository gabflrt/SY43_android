package com.example.sy43_real_estate_application

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text("Logo") },
        actions = {
            if (currentRoute != "login") {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Home")
                }
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Log in")
                }
            }
        }
    )
}
