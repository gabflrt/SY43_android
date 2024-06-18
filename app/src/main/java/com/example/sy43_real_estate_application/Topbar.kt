package com.example.sy43_real_estate_application

import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun TopBar(navController: NavHostController, userViewModel: UserViewModel, modifier: Modifier = Modifier) {
    val user = userViewModel.user.value
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
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
            Text(
                text = "Welcome ${user?.firstName ?: ""}!",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
            Button(onClick = { navController.navigate("wishlist") }) {
                Image(
                    painter = painterResource(id = R.drawable.wishlist),
                    contentDescription = "Wishlist Image",
                    modifier = Modifier.size(35.dp)
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
