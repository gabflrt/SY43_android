package com.example.sy43_real_estate_application.model

import com.google.gson.annotations.SerializedName

data class ImmoProperty(
    val url: String,
    val prix: Long?,
    val surface: Double?,
    val charges: Long?,
    val taxe_fonciere: Long?,
    val dpe: Int?,
    @SerializedName("insertion_date")
    val insertionDate: String,
    val photos: List<ImmoProperty>
)
