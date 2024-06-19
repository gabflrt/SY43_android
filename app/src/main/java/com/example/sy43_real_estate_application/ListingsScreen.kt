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
    var sortByPrice by remember { mutableStateOf(SortOrder.NONE) }
    var sortByDpe by remember { mutableStateOf(SortOrder.NONE) }
    var sortBySurface by remember { mutableStateOf(SortOrder.NONE) }

    var tempSortByPrice by remember { mutableStateOf(SortOrder.NONE) }
    var tempSortByDpe by remember { mutableStateOf(SortOrder.NONE) }
    var tempSortBySurface by remember { mutableStateOf(SortOrder.NONE) }

    val listingsState = produceState<List<ImmoProperty>>(initialValue = emptyList()) {
        value = fetchProperties()
    }

    val sortedListings = remember(listingsState.value, sortByPrice, sortByDpe, sortBySurface) {
        applySorts(listingsState.value, sortByPrice, sortByDpe, sortBySurface)
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
                            // Initialize temporary sort values with current sort values
                            tempSortByPrice = sortByPrice
                            tempSortBySurface = sortBySurface
                            tempSortByDpe = sortByDpe
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
                    SortOption(
                        title = "Price",
                        sortOrder = tempSortByPrice,
                        onSortOrderChange = { tempSortByPrice = it }
                    )
                    SortOption(
                        title = "Surface",
                        sortOrder = tempSortBySurface,
                        onSortOrderChange = { tempSortBySurface = it }
                    )
                    SortOption(
                        title = "DPE",
                        sortOrder = tempSortByDpe,
                        onSortOrderChange = { tempSortByDpe = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Apply the temporary sort values to the actual sort values
                        sortByPrice = tempSortByPrice
                        sortBySurface = tempSortBySurface
                        sortByDpe = tempSortByDpe
                        showSortDialog = false
                    }
                ) {
                    Text("Apply Sort")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showSortDialog = false }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SortOption(
    title: String,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Row {
            RadioButton(
                selected = sortOrder == SortOrder.ASCENDING,
                onClick = {
                    onSortOrderChange(
                        if (sortOrder == SortOrder.ASCENDING) SortOrder.NONE else SortOrder.ASCENDING
                    )
                }
            )
            Text("Ascending")
            RadioButton(
                selected = sortOrder == SortOrder.DESCENDING,
                onClick = {
                    onSortOrderChange(
                        if (sortOrder == SortOrder.DESCENDING) SortOrder.NONE else SortOrder.DESCENDING
                    )
                }
            )
            Text("Descending")
        }
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
    sortByPrice: SortOrder,
    sortByDpe: SortOrder,
    sortBySurface: SortOrder
): List<ImmoProperty> {
    var sortedListings = listings

    sortedListings = when (sortByPrice) {
        SortOrder.ASCENDING -> sortedListings.sortedBy { it.prix }
        SortOrder.DESCENDING -> sortedListings.sortedByDescending { it.prix }
        SortOrder.NONE -> sortedListings
    }

    sortedListings = when (sortBySurface) {
        SortOrder.ASCENDING -> sortedListings.sortedBy { it.surface }
        SortOrder.DESCENDING -> sortedListings.sortedByDescending { it.surface }
        SortOrder.NONE -> sortedListings
    }

    sortedListings = when (sortByDpe) {
        SortOrder.ASCENDING -> sortedListings.sortedBy { it.dpe }
        SortOrder.DESCENDING -> sortedListings.sortedByDescending { it.dpe }
        SortOrder.NONE -> sortedListings
    }

    return sortedListings
}
