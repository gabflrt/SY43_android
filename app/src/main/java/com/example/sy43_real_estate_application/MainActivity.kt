package com.example.sy43_real_estate_application

import  android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewModelScope
import com.example.sy43_real_estate_application.data.datasource.InventoryDatabase
import com.example.sy43_real_estate_application.ui.theme.SY43_real_estate_applicationTheme
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.example.sy43_real_estate_application.data.datasource.User
import com.example.sy43_real_estate_application.data.datasource.UserDAO
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SY43_real_estate_applicationTheme {

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(context = applicationContext)
                }
            }
        }
        /*val database = InventoryDatabase.getDatabase(applicationContext)
        val propertyDao = database.userDao()
        // Exemple d'insertion de données
        val property = User(email = "test@example.com", firstName =  "John", lastName =  "Doe")

        lifecycleScope.launch(Dispatchers.IO) {
            propertyDao.insertUser(property)
        }*/
    }
}