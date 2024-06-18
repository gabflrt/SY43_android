package com.example.sy43_real_estate_application.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WishlistDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(wishlistItem: WishlistItem)

    @Delete
    suspend fun deleteWishlistItem(wishlistItem: WishlistItem)

    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    fun getWishlistByUserId(userId: Int): List<WishlistItem>
}
