package com.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ListingsScreen(navController: NavHostController) {
    var listings by remember { mutableStateOf(generateListings()) }
    var showDialog by remember { mutableStateOf(false) }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    ScreenContent(navController = navController) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Listings",
                    fontSize = 24.sp
                )
                Button(
                    onClick = { showDialog = true },
                ) {
                    Text("Filter")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listings) { listing ->
                    ListingItem(
                        id = listing.id,
                        city = listing.city,
                        agency = listing.agency,
                        price = listing.price
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Filters") },
            text = {
                Column {
                    OutlinedTextField(
                        value = minPrice,
                        onValueChange = { minPrice = it },
                        label = { Text("Minimum Price") },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = maxPrice,
                        onValueChange = { maxPrice = it },
                        label = { Text("Maximum Price") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        listings = applyPriceFilter(minPrice, maxPrice)
                        showDialog = false
                    },
                ) {
                    Text("Apply Filter")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                ) {
                    Text("Close")
                }
            }
        )
    }
}

fun generateListings(): List<Listing> {
    return List(10) { index ->
        Listing(
            id = index + 1,
            city = "City ${index + 1}",
            agency = "Agency ${index + 1}",
            price = "${(index + 1) * 1000} €"
        )
    }
}

fun applyPriceFilter(minPrice: String, maxPrice: String): List<Listing> {
    val min = if (minPrice.isNotEmpty()) minPrice.toInt() else Int.MIN_VALUE
    val max = if (maxPrice.isNotEmpty()) maxPrice.toInt() else Int.MAX_VALUE

    return generateListings().filter { listing ->
        val price = listing.price.substringBefore(" €").toInt()
        price in min..max
    }
}

data class Listing(val id: Int, val city: String, val agency: String, val price: String)
