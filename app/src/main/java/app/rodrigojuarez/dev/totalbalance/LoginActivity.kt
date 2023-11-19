package app.rodrigojuarez.dev.totalbalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import app.rodrigojuarez.dev.totalbalance.storage.Authenticator
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var authenticator: Authenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authenticator = Authenticator(this)

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val editUsername: EditText = findViewById(R.id.editTextUsername)
        val editPassword: EditText = findViewById(R.id.editTextPassword)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val circularProgressIndicator: CircularProgressIndicator =
            findViewById(R.id.circularProgressIndicator)

        btnLogin.setOnClickListener {
            val username: String = editUsername.text.toString()
            val password: String = editPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                btnLogin.visibility = View.GONE
                circularProgressIndicator.visibility = View.VISIBLE

                authenticator.authenticate(username, password) { isAuthenticated ->
                    btnLogin.visibility = View.VISIBLE
                    circularProgressIndicator.visibility = View.GONE
                    if (isAuthenticated) {
                        Log.d("Auth", "Login successful")
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        authenticator.saveLoginState(true)
                        finish()
                    } else {
                        Log.d("Auth", "Invalid credentials")
                        Snackbar.make(it, "Credenciales inválidas", Snackbar.LENGTH_LONG).show()
                    }
                }
            } else {
                Snackbar.make(it, "Por favor, completá todos los campos", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}