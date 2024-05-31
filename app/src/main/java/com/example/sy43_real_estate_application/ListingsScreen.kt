package com.example.sy43_real_estate_application

import UserViewModel
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
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var minPrice by remember { mutableStateOf(0f) }
    var maxPrice by remember { mutableStateOf(10000f) }
    var cityFilter by remember { mutableStateOf("") }
    var sortByPriceAscending by remember { mutableStateOf(true) }


    ScreenContent(navController = navController, userViewModel = UserViewModel()) {
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
                    onClick = { showSortDialog = true },
                ) {
                    Text("Sort")
                }
                Button(
                    onClick = { showFilterDialog = true },
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

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text(text = "Filters") },
            text = {
                Column {
                    Text("Minimum Price: ${minPrice.toInt()} €")
                    Slider(
                        value = minPrice,
                        onValueChange = { minPrice = it },
                        valueRange = 0f..10000f,
                        steps = 9,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text("Maximum Price: ${maxPrice.toInt()} €")
                    Slider(
                        value = maxPrice,
                        onValueChange = { maxPrice = it },
                        valueRange = 0f..10000f,
                        steps = 9,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = cityFilter,
                        onValueChange = { cityFilter = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        listings = applyFilters(minPrice.toInt(), maxPrice.toInt(), cityFilter)
                        showFilterDialog = false
                    },
                ) {
                    Text("Apply Filter")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showFilterDialog = false },
                ) {
                    Text("Close")
                }
            }
        )
    }

    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text(text = "Sort by") },
            text = {
                Column {
                    // Options de tri
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        RadioButton(
                            selected = sortByPriceAscending,
                            onClick = { sortByPriceAscending = true }
                        )
                        Text("Price Ascending")
                    }
                    Row {
                        RadioButton(
                            selected = !sortByPriceAscending,
                            onClick = { sortByPriceAscending = false }
                        )
                        Text("Price Descending")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        listings = applySort(sortByPriceAscending)
                        showSortDialog = false
                    },
                ) {
                    Text("Apply Sort")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showSortDialog = false },
                ) {
                    Text("Close")
                }
            }
        )
    }
}

fun generateListings(): List<Listing> {
    return List(12) { index ->
        Listing(
            id = index + 1,
            city = "City ${index + 1}",
            agency = "Agency ${index + 1}",
            price = "${(index + 1) * 1000} €"
        )
    }
}

fun applyFilters(minPrice: Int, maxPrice: Int, city: String): List<Listing> {
    return generateListings().filter { listing ->
        val price = listing.price.substringBefore(" €").toInt()
        val matchesPrice = price in minPrice..maxPrice
        val matchesCity = city.isEmpty() || listing.city.contains(city, ignoreCase = true)
        matchesPrice && matchesCity
    }
}

fun applySort(sortByPriceAscending: Boolean): List<Listing> {
    val sortedListings = generateListings().toMutableList()
    sortedListings.sortBy { it.price }
    if (!sortByPriceAscending) {
        sortedListings.reverse()
    }
    return sortedListings
}

data class Listing(val id: Int, val city: String, val agency: String, val price: String)
