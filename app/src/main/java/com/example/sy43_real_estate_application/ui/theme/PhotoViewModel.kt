package com.example.sy43_real_estate_application.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sy43_real_estate_application.network.RealEstateApi
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    fun getPhotos() {
        viewModelScope.launch {
            val listResult = RealEstateApi.retrofitService.getProperties()
            println(listResult)

        }
    }
}

