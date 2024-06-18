package com.example.sy43_real_estate_application

import UserViewModel
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sy43_real_estate_application.data.datasource.WishlistItem
import com.example.sy43_real_estate_application.model.ImmoProperty

@Composable
fun WishlistScreen(navController: NavHostController, userViewModel: UserViewModel, context: Context) {
    val user = userViewModel.user.value
    val wishlistItems by userViewModel.wishlistItems

    ScreenContent(navController = navController, userViewModel = userViewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            LaunchedEffect(user) {
                if (user != null) {
                    userViewModel.getWishlistByUserId(user.id, context)
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(wishlistItems) { item ->
                    ListingItem(
                        image = item.imageUrl,
                        prix = item.prix?.toString() ?: "N/A",
                        surface = item.surface?.toString() ?: "N/A",
                        dpe = item.dpe ?: 0,
                        isWishlisted = true,
                        onWishlistToggle = {
                            userViewModel.removeFromWishlist(
                                user?.id ?: 0, ImmoProperty(
                                    url = item.propertyId,
                                    ville = "",
                                    departement = null,
                                    prix = item.prix,
                                    surface = item.surface,
                                    charges = null,
                                    taxe_fonciere = null,
                                    dpe = item.dpe,
                                    image = item.imageUrl
                                ), context
                            )
                        }
                    )
                }
            }
        }
    }
}