package com.example.sy43_real_estate_application.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImmoPhoto(
    val id: String,
    @SerialName("img_src") val imgSrc: String
)
