import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.sy43_real_estate_application.data.datasource.InventoryDatabase
import com.example.sy43_real_estate_application.data.datasource.User

class UserViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    fun loginUser(email: String, password: String, context: Context) {
        val database = InventoryDatabase.getDatabase(context)
        val userDao = database.userDao()

        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUser(email, password)
            _user.value = user
        }
    }

    fun logout() {
        _user.value = null
    }
}
