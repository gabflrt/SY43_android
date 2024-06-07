package com.example.sy43_real_estate_application.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName
import com.example.sy43_real_estate_application.model.ImmoProperty

// URL de base pour l'API
private const val BASE_URL = "https://apparts.quickapi.io/api/"

// Création de l'instance Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// Interface définissant l'API
interface RealEstateApiService {
    @GET("data?qapikey=719f4507-cdba-4882-a8ba-9fdbeeebc27d")
    suspend fun getProperties(): ApiResponse
}

// Objet pour accéder à l'API
object RealEstateApi {
    val retrofitService: RealEstateApiService by lazy {
        retrofit.create(RealEstateApiService::class.java)
    }
}

// Classe représentant la réponse de l'API
data class ApiResponse(
    val meta: List<Meta>,
    val data: List<ImmoProperty>
)

data class Meta(
    val name: String,
    val type: String
)

