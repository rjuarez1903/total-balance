package app.rodrigojuarez.dev.totalbalance.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Authenticator(private val context: Context) {

    fun authenticate(username: String, password: String, callback: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            val isAuthenticated = username == "admin" && password == "123456"
            callback(isAuthenticated)
        }
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    fun logout() {
        saveLoginState(false)
    }

}
