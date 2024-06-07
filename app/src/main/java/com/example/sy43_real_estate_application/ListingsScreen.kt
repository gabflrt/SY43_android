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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

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
                        image = listing.image ?: "https://www.century21agencedutheatre.com/imagesBien/s3/202/793/c21_202_793_28224_1_6A7F1FA6-CB6E-4117-98D4-9FC799BDA715.jpg",
                        prix = listing.prix?.toString() ?: "N/A",
                        surface = listing.surface?.toString() ?: "N/A",
                        dpe = listing.dpe ?: 0
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
                ville = property.ville,
                departement = property.departement,
                surface = property.surface,
                charges = property.charges,
                taxe_fonciere = property.taxe_fonciere,
                dpe = property.dpe,
                image = property.image
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
fun ListingItem(image: String, prix: String, surface: String, dpe: Int) {
    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberImagePainter(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Prix: $prix €")
        Text(text = "Surface: $surface m²")
        if(dpe == 0){
            Text(text = "DPE: Non disponible")
        } else if(dpe == 1){
            Text(text = "DPE: A")
        } else if(dpe == 2) {
            Text(text = "DPE: B")
        } else if(dpe == 3) {
            Text(text = "DPE: C")
        } else if(dpe == 4) {
            Text(text = "DPE: D")
        } else if(dpe == 5) {
            Text(text = "DPE: E")
        } else if(dpe == 6) {
            Text(text = "DPE: F")
        } else if(dpe == 7) {
            Text(text = "DPE: G")
        }

    }
}
