package com.school.projettm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.school.projettm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jwtSharedPrefs = getSharedPreferences("JWT", MODE_PRIVATE)
        val editor = jwtSharedPrefs.edit()
        editor.clear()
        editor.apply()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val logoutButton = findViewById<Button>(R.id.btn_mainActivity_logout)

        logoutButton.setOnClickListener {
            logout()
        }
        setUpListeners()
    }

    private fun logout() {
        // delete the token
        val jwtSharedPrefs = getSharedPreferences("JWT", MODE_PRIVATE)
        val editor = jwtSharedPrefs.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show()
    }

    private fun setUpListeners() {
        binding.btnMainActivityGoToArticleList.setOnClickListener {
            Intent(this, RequestActivityArticles::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnMainActivityGoToSellArticle.setOnClickListener {
            Intent(this, SellArticleActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnMainActivityGoToLoginPage.setOnClickListener {
            Intent(this, LoginFormActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}