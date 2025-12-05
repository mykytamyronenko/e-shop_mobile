package com.school.projettm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.school.projettm.dtos.GetAllArticleResponse
import com.school.projettm.models.Article

class ArticleListFragment : Fragment() {
    private val articles: ArrayList<Article> = arrayListOf()
    private val articleRecyclerViewAdapter = ArticleRecyclerViewAdapter(
        articles,
        object : ArticleRecyclerViewAdapter.ArticleRecyclerViewAdapterCallback {
            override fun onArticleClick(article: Article) {
                Intent(requireContext(), ArticleFragmentContainerActivity::class.java).apply {
                    putExtra("ARTICLE_ID", article.articleId)
                    putExtra("ARTICLE_IMAGE_PATH", article.mainImageUrl)
                    putExtra("ARTICLE_DESCRIPTION", article.description)
                    putExtra("ARTICLE_TITLE", article.title)
                    putExtra("ARTICLE_CREATEDAT", article.updatedAt)

                    startActivity(this)
                }
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = articleRecyclerViewAdapter
            }
        }

        return view
    }
    
    fun initUIWithArticles(articleList: List<Article>) {
        if (articles.isEmpty()) {
            articles.addAll(articleList)
            articleRecyclerViewAdapter.notifyDataSetChanged()
        }
    }


    companion object {
        fun newInstance() = ArticleListFragment()
    }
}