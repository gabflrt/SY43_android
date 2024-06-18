package com.example.sy43_real_estate_application.data.repository

import com.example.sy43_real_estate_application.data.datasource.User
import com.example.sy43_real_estate_application.data.datasource.UserDAO
import com.example.sy43_real_estate_application.data.datasource.WishlistDAO
import com.example.sy43_real_estate_application.data.datasource.WishlistItem
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDAO, private val wishlistDao: WishlistDAO){
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    fun deleteUserById(id: Int) = userDao.deleteUserById(id)

    fun getAllUsers() = userDao.getAllUsers()

    fun getUserById(id: Int) = userDao.getUserById(id)

    suspend fun getUser(email: String, password: String) = userDao.getUser(email, password)

    // Methods related to the wishlist

    suspend fun insertWishlistItem(wishlistItem: WishlistItem) = wishlistDao.insertWishlistItem(wishlistItem)

    suspend fun deleteWishlistItem(wishlistItem: WishlistItem) = wishlistDao.deleteWishlistItem(wishlistItem)

    fun getWishlistByUserId(userId: Int) = wishlistDao.getWishlistByUserId(userId)
}