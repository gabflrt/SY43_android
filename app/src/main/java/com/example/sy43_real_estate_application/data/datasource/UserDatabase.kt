package com.example.sy43_real_estate_application.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, WishlistItem::class], version = 5, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun wishlistDao(): WishlistDAO

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
