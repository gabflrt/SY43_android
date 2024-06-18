import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.sy43_real_estate_application.data.datasource.InventoryDatabase
import com.example.sy43_real_estate_application.data.datasource.User
import com.example.sy43_real_estate_application.data.datasource.WishlistItem
import com.example.sy43_real_estate_application.model.ImmoProperty

class UserViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _loginError = mutableStateOf(false)
    val loginError: State<Boolean> = _loginError

    fun loginUser(email: String, password: String, context: Context) {
        val database = InventoryDatabase.getDatabase(context)
        val userDao = database.userDao()

        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUser(email, password)
            if (user != null) {
                _user.value = user
                _loginError.value = false
            } else {
                _loginError.value = true
            }
        }
    }

    fun logout() {
        _user.value = null
    }

    private val _wishlistItems = mutableStateOf<List<WishlistItem>>(emptyList())
    val wishlistItems: State<List<WishlistItem>> = _wishlistItems

    fun getWishlistByUserId(userId: Int, context: Context) {
        val database = InventoryDatabase.getDatabase(context)
        val wishlistDao = database.wishlistDao()

        viewModelScope.launch(Dispatchers.IO) {
            _wishlistItems.value = wishlistDao.getWishlistByUserId(userId)
        }
    }

    fun addToWishlist(userId: Int, property: ImmoProperty, context: Context) {
        val database = InventoryDatabase.getDatabase(context)
        val wishlistDao = database.wishlistDao()

        viewModelScope.launch(Dispatchers.IO) {
            val wishlistItem = WishlistItem(
                userId = userId,
                propertyId = property.url,
                imageUrl = property.image ?: "",
                prix = property.prix,
                surface = property.surface,
                dpe = property.dpe
            )
            wishlistDao.insertWishlistItem(wishlistItem)
            _wishlistItems.value = wishlistDao.getWishlistByUserId(userId)
        }
    }

    fun removeFromWishlist(userId: Int, property: ImmoProperty, context: Context) {
        val database = InventoryDatabase.getDatabase(context)
        val wishlistDao = database.wishlistDao()

        viewModelScope.launch(Dispatchers.IO) {
            val wishlistItem = wishlistDao.getWishlistByUserId(userId).find { it.propertyId == property.url }
            if (wishlistItem != null) {
                wishlistDao.deleteWishlistItem(wishlistItem)
                _wishlistItems.value = wishlistDao.getWishlistByUserId(userId)
            }
        }
    }

    fun isWishlisted(userId: Int, propertyId: String): Boolean {
        return _wishlistItems.value.any { it.userId == userId && it.propertyId == propertyId }
    }
}

