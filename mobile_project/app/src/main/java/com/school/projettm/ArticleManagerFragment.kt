package com.school.projettm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.school.projettm.databinding.FragmentArticleManagerBinding
import com.school.projettm.models.Article

class ArticleManagerFragment : Fragment() {
    private lateinit var binding: FragmentArticleManagerBinding

    private val viewModel: ArticleManagerViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleManagerBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("Articles", "onStart() de ArticleManagerFragment appelé.")
        val articleListFragment = childFragmentManager
            .findFragmentById(R.id.fragmentContainerView_articleManagerFragment_articleListFragment) as ArticleListFragment

        viewModel.mutableArticleLiveData.observe(viewLifecycleOwner) {
            Log.i("Articles", it.toString())
            articleListFragment.initUIWithArticles(it.map { Article(it.articleId,
                it.title,
                it.description,
                it.price,
                it.category,
                it.state,
                it.userId,
                it.createdAt,
                it.updatedAt,
                it.status,
                it.mainImageUrl,
                it.quantity) }.toList())
            Log.d("ArticleListFragment", "Articles loaded: ${it.size}")
        }

        viewModel.startGetAllArticles()

    }



    companion object {
        fun newInstance() = ArticleManagerFragment()
    }
}