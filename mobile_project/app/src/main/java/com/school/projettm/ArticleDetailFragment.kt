package com.school.projettm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.school.projettm.databinding.FragmentArticleDetailBinding
import com.school.projettm.models.Article
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ArticleDetailFragment : Fragment() {
    lateinit var binding: FragmentArticleDetailBinding
    private var articleId: Int = -1
    private var articleImagePath: String? = null
    private var articleDescription: String? = null
    private var articleTitle: String? = null
    private var articleCreatedAt: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getInt(ARG_ARTICLE_ID)
            articleImagePath = "http://10.0.2.2:5185/"+it.getString(ARG_ARTICLE_IMAGE_PATH)
            articleDescription = it.getString(ARG_ARTICLE_DESCRIPTION)
            articleTitle = it.getString(ARG_ARTICLE_TITLE)
            articleCreatedAt = it.getString(ARG_ARTICLE_CREATEDAT)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        displayArticleDetails(articleId,articleImagePath,articleDescription,articleTitle,articleCreatedAt)
        return binding.root
    }

    private fun displayArticleDetails(articleId: Int, imagePath: String?, description: String?,articleTitle: String?,articleCreatedAt: String?) {
        val correctedImagePath = imagePath?.replace("\\", "/")
        binding.tvArticleItemFragmentDescription.text = "Description: $description"
        binding.tvArticleItemFragmentTitle.text = "$articleTitle"

        if(articleCreatedAt !=null)
        {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(articleCreatedAt)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.HOUR_OF_DAY, 1)  // Ajouter 1 heure

                val outputFormat = SimpleDateFormat("dd/MM/yyyy 'at' HH:mm", Locale.getDefault())

                val formattedDate = outputFormat.format(calendar.time)
                binding.tvArticleItemFragmentCreatedAt.text = "This article was uploaded at : $formattedDate"
            }

        }

        Glide.with(this)
            .load(correctedImagePath)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.ivArticleImage)


    }

    companion object {
        private const val ARG_ARTICLE_ID = "article_id"
        private const val ARG_ARTICLE_IMAGE_PATH = "article_image_path"
        private const val ARG_ARTICLE_DESCRIPTION = "article_description"
        private const val ARG_ARTICLE_TITLE = "article_title"
        private const val ARG_ARTICLE_CREATEDAT = "article_createdAt"


        fun newInstance(articleId: Int, imagePath: String, articleDescription: String,articleTitle:String,articleCreatedAt:String) = ArticleDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ARTICLE_ID, articleId)
                putString(ARG_ARTICLE_IMAGE_PATH, imagePath)
                putString(ARG_ARTICLE_DESCRIPTION, articleDescription)
                putString(ARG_ARTICLE_TITLE, articleTitle)
                putString(ARG_ARTICLE_CREATEDAT, articleCreatedAt)

            }
        }
    }
}