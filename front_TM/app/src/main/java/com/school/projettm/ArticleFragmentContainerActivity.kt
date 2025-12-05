package com.school.projettm

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ArticleFragmentContainerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_article_fragment_container)

        val articleIdOfIntent = intent.getIntExtra("ARTICLE_ID",-1)
        val articleImagePath = intent.getStringExtra("ARTICLE_IMAGE_PATH")
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION")
        val articleTitle = intent.getStringExtra("ARTICLE_TITLE")
        val articleCreatedAt = intent.getStringExtra("ARTICLE_CREATEDAT")


        if (articleIdOfIntent != -1 && !articleImagePath.isNullOrEmpty() && !articleDescription.isNullOrEmpty() && !articleTitle.isNullOrEmpty() && !articleCreatedAt.isNullOrEmpty()) {
            val fragment = ArticleDetailFragment.newInstance(articleIdOfIntent,articleImagePath,articleDescription,articleTitle,articleCreatedAt)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        } else {
            Toast.makeText(this, "No id has been found", Toast.LENGTH_SHORT).show()
        }


    }
}