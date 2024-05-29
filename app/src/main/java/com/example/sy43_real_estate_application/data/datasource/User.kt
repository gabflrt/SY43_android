package com.example.sy43_real_estate_application.data.datasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    val email : String,
    val firstName : String,
    val lastName : String,
    val isPassed: Boolean
)
