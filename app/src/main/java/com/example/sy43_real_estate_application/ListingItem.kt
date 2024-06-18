package com.example.sy43_real_estate_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import kotlin.math.roundToInt

@Composable
fun ListingItem(
    navController: NavHostController,
    image: String,
    prix: String,
    surface: String,
    dpe: Int,
    isWishlisted: Boolean,
    onWishlistToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                   // .clickable { navController.navigate("flat/$id") }
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "BELFORT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Image(
                        painter = rememberImagePainter(image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Price: ${prix.toDouble().roundToInt()} €")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Surface: $surface m²")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (dpe) {
                            1 -> "DPE: A"
                            2 -> "DPE: B"
                            3 -> "DPE: C"
                            4 -> "DPE: D"
                            5 -> "DPE: E"
                            6 -> "DPE: F"
                            7 -> "DPE: G"
                            8 -> "DPE: Not available"
                            else -> "DPE: Unknown"
                        }
                    )
                }
            }
            Button(onClick = onWishlistToggle) {
                Text(if (isWishlisted) "Remove from Wishlist" else "Add to Wishlist")
            }
        }
    }

}
