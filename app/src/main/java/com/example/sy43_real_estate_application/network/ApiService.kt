package com.example.sy43_real_estate_application.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

// URL de base pour l'API
private const val BASE_URL = "https://immo.quickapi.io/api/"

// Création de l'instance Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// Interface définissant l'API
interface RealEstateApiService {
    @GET("data?qapikey=33b4971d-6833-4cc6-ac45-f3483c0d79b3")
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
    val data: List<Property>
)

data class Meta(
    val name: String,
    val type: String
)

data class Property(
    val url: String,
    val prix: Long?,
    val surface: Double?,
    val charges: Long?,
    val taxe_fonciere: Long?,
    val dpe: Int?,
    @SerializedName("insertion_date")
    val insertionDate: String
)
