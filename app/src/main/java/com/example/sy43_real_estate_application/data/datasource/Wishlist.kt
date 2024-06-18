package com.example.sy43_real_estate_application.data.datasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val propertyId: String,
    val imageUrl: String,
    val prix: Double?,
    val surface: Double?,
    val dpe: Int?
)