package com.example.sy43_real_estate_application.model

import com.google.gson.annotations.SerializedName

data class ImmoProperty(
    val url: String,
    val prix: Double?,
    val surface: Double?,
    val charges: Double?,
    val taxe_fonciere: Double?,
    val dpe: Int?,
    val photos: List<ImmoProperty>
)
