package com.example.sy43_real_estate_application

import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.sy43_real_estate_application.model.ImmoProperty
import com.example.sy43_real_estate_application.network.RealEstateApi
import retrofit2.HttpException
import java.io.IOException

@Composable
fun ListingsScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var showSortDialog by remember { mutableStateOf(false) }
    var sortByPriceAscending by remember { mutableStateOf(true) }
    var sortByDpeAscending by remember { mutableStateOf(true) }
    var sortBySurfaceAscending by remember { mutableStateOf(true) }

    // Temporary state for sort selection in the dialogs
    var tempSortByPriceAscending by remember { mutableStateOf(sortByPriceAscending) }
    var tempSortByDpeAscending by remember { mutableStateOf(sortByDpeAscending) }
    var tempSortBySurfaceAscending by remember { mutableStateOf(sortBySurfaceAscending) }

    val listingsState = produceState<List<ImmoProperty>>(initialValue = emptyList()) {
        value = fetchProperties()
    }

    // Apply the sorts on the listings
    val sortedListings = remember(listingsState.value, sortByPriceAscending, sortByDpeAscending, sortBySurfaceAscending) {
        applySorts(listingsState.value, sortByPriceAscending, sortByDpeAscending, sortBySurfaceAscending)
    }

    val user = userViewModel.user.value
    val context = LocalContext.current

    ScreenContent(navController = navController, userViewModel = userViewModel) {
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
                Row {
                    Button(
                        onClick = {
                            // Set temporary sort states to current sort states when opening the dialog
                            tempSortByPriceAscending = sortByPriceAscending
                            tempSortByDpeAscending = sortByDpeAscending
                            tempSortBySurfaceAscending = sortBySurfaceAscending
                            showSortDialog = true
                        },
                    ) {
                        Text("Sort")
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedListings) { listing ->
                    val isWishlisted = userViewModel.isWishlisted(user?.id ?: 0, listing.url)
                    ListingItem(
                        navController = navController,
                        image = listing.image ?: "https://www.century21agencedutheatre.com/imagesBien/s3/202/793/c21_202_793_28224_1_6A7F1FA6-CB6E-4117-98D4-9FC799BDA715.jpg",
                        prix = listing.prix?.toString() ?: "N/A",
                        surface = listing.surface?.toString() ?: "N/A",
                        dpe = listing.dpe ?: 0,
                        isWishlisted = isWishlisted,
                        onWishlistToggle = {
                            if (isWishlisted) {
                                userViewModel.removeFromWishlist(user?.id ?: 0, listing, context)
                            } else {
                                userViewModel.addToWishlist(user?.id ?: 0, listing, context)
                            }
                        },
                        propertyUrl = listing.url
                    )
                }
            }
        }
    }

    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text(text = "Sort by") },
            text = {
                Column {
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        RadioButton(
                            selected = tempSortByPriceAscending,
                            onClick = { tempSortByPriceAscending = true }
                        )
                        Text("Price Ascending")
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        RadioButton(
                            selected = !tempSortByPriceAscending,
                            onClick = { tempSortByPriceAscending = false }
                        )
                        Text("Price Descending")
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp).padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = tempSortBySurfaceAscending,
                            onClick = { tempSortBySurfaceAscending = true }
                        )
                        Text("Surface Ascending")
                    }
                    Row(Modifier.padding(bottom = 4.dp)) {
                        RadioButton(
                            selected = !tempSortBySurfaceAscending,
                            onClick = { tempSortBySurfaceAscending = false }
                        )
                        Text("Surface Descending")
                    }
                    Row(Modifier.padding(bottom = 4.dp).padding(top = 8.dp)) {
                        RadioButton(
                            selected = tempSortByDpeAscending,
                            onClick = { tempSortByDpeAscending = true }
                        )
                        Text("DPE Ascending")
                    }
                    Row(Modifier.padding(bottom = 4.dp)) {
                        RadioButton(
                            selected = !tempSortByDpeAscending,
                            onClick = { tempSortByDpeAscending = false }
                        )
                        Text("DPE Descending")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Update the main sort states when the user confirms
                        sortByPriceAscending = tempSortByPriceAscending
                        sortBySurfaceAscending = tempSortBySurfaceAscending
                        sortByDpeAscending = tempSortByDpeAscending
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

suspend fun fetchProperties(): List<ImmoProperty> {
    return try {
        val response = RealEstateApi.retrofitService.getProperties()
        response.data.map { property ->
            val dpe = if (property.dpe == 0) 8 else property.dpe
            ImmoProperty(
                url = property.url,
                prix = property.prix,
                ville = property.ville,
                departement = property.departement,
                surface = property.surface,
                charges = property.charges,
                taxe_fonciere = property.taxe_fonciere,
                dpe = dpe,
                image = property.image
            )
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    } catch (e: HttpException) {
        e.printStackTrace()
        emptyList()
    }
}

fun applySorts(
    listings: List<ImmoProperty>,
    sortByPriceAscending: Boolean,
    sortByDpeAscending: Boolean,
    sortBySurfaceAscending: Boolean
): List<ImmoProperty> {
    var sortedListings = listings

    if (sortByPriceAscending) {
        sortedListings = sortedListings.sortedBy { it.prix }
    } else {
        sortedListings = sortedListings.sortedByDescending { it.prix }
    }

    if (sortBySurfaceAscending) {
        sortedListings = sortedListings.sortedBy { it.surface }
    } else {
        sortedListings = sortedListings.sortedByDescending { it.surface }
    }

    if (sortByDpeAscending) {
        sortedListings = sortedListings.sortedBy { it.dpe }
    } else {
        sortedListings = sortedListings.sortedByDescending { it.dpe }
    }

    return sortedListings
}

