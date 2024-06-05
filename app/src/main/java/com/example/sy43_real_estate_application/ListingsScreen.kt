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
import com.example.sy43_real_estate_application.model.ImmoProperty
import com.example.sy43_real_estate_application.network.RealEstateApi
import retrofit2.HttpException
import java.io.IOException

@Composable
fun ListingsScreen(navController: NavHostController) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var minPrice by remember { mutableStateOf(0f) }
    var maxPrice by remember { mutableStateOf(10000f) }
    var cityFilter by remember { mutableStateOf("") }
    var sortByPriceAscending by remember { mutableStateOf(true) }

    // Utiliser `produceState` pour gérer l'état des propriétés récupérées de l'API
    val listingsState = produceState<List<ImmoProperty>>(initialValue = emptyList()) {
        value = fetchProperties()
    }

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
                items(listingsState.value) { listing ->
                    ListingItem(
                        id = listing.url,
                        prix = listing.prix?.toString() ?: "N/A",
                        surface = listing.surface?.toString() ?: "N/A",
                        dpe = listing.dpe?.toString() ?: "N/A"
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
                        // Mettez à jour les listings ici avec les filtres appliqués
                        // listings = applyFilters(minPrice.toInt(), maxPrice.toInt(), cityFilter)
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
                        // Mettez à jour les listings ici avec le tri appliqué
                        // listings = applySort(sortByPriceAscending)
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
            ImmoProperty(
                url = property.url,
                prix = property.prix,
                surface = property.surface,
                charges = property.charges,
                taxe_fonciere = property.taxe_fonciere,
                dpe = property.dpe,
                photos = emptyList()  // Ajoutez ici les photos si nécessaire
            )
        }
    } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
    } catch (e: HttpException) {
        // Gérez les erreurs HTTP ici
        e.printStackTrace()
        emptyList()
    }
}

@Composable
fun ListingItem(id: String, prix: String, surface: String, dpe: String) {
    // Exemple de mise en page simple pour ListingItem, ajustez selon vos besoins
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "ID: $id")
        Text(text = "Prix: $prix")
        Text(text = "Surface: $surface")
        Text(text = "DPE: $dpe")
    }
}
