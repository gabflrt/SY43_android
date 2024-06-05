package com.example.sy43_real_estate_application.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

// URL de base pour l'API
private const val BASE_URL = "https://immo-excel.quickapi.io/api/"

// Création de l'instance Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// Interface définissant l'API
interface RealEstateApiService {
    @GET("data?qapikey=39e5a6e8-cf24-46a5-8005-dc3443a0c673")
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
    val prix: Double?,
    val surface: Double?,
    val charges: Double?,
    val taxe_fonciere: Double?,
    val dpe: Int?
)
