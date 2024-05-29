package com.example.sy43_real_estate_application.data.repository

import com.example.sy43_real_estate_application.data.datasource.User
import com.example.sy43_real_estate_application.data.datasource.UserDAO
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDAO){
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    fun deleteUserById(id: Int) = userDao.deleteUserById(id)

    fun getAllUsers() = userDao.getAllUsers()

    fun getUserById(id: Int) = userDao.getUserById(id)
}