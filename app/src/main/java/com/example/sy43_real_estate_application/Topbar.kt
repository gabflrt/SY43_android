package com.example.sy43_real_estate_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun TopBar(navController: NavHostController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary, // Utilisez la couleur du thème pour les boutons
                shape = RoundedCornerShape(8.dp) // Bordure arrondie
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.navigate("home") }) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "House Image",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Button(onClick = { navController.navigate("login") }) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "User Image",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
