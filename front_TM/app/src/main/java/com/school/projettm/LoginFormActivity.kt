package com.school.projettm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.school.projettm.models.LoginRequest
import com.school.projettm.models.LoginResponse
import com.school.projettm.utils.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class LoginFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)

        val usernameEditText = findViewById<EditText>(R.id.et_loginFormActivity_username)
        val passwordEditText = findViewById<EditText>(R.id.et_loginFormActivity_password)
        val loginButton = findViewById<Button>(R.id.btn_loginFormActivity_login)

        val jwtSharedPrefs = getSharedPreferences("JWT", MODE_PRIVATE)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val loginRequest = LoginRequest(username, password)


            RetrofitFactory.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.token

                        if (token != null) {
                            val editor = jwtSharedPrefs.edit()
                            editor.putString("value", token)
                            editor.apply()
                            Log.d("TOKEN_RECEIVED", "Token received from server: $token")
                            Toast.makeText(this@LoginFormActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(this@LoginFormActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginFormActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("loginfailed", "error: ${t.message}")
                }
            })
        }
    }
}

