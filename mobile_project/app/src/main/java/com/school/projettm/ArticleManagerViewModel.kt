package com.school.projettm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.school.projettm.dtos.CreateArticleCommand
import com.school.projettm.dtos.CreateArticleResponse
import com.school.projettm.dtos.GetAllArticleResponse
import com.school.projettm.repositories.IArticleRepository
import com.school.projettm.utils.RetrofitFactory
import kotlinx.coroutines.launch
import java.util.Date

class ArticleManagerViewModel: ViewModel() {
    private val articleRepository = RetrofitFactory.instance.create(IArticleRepository::class.java)

    val mutableArticleLiveData: MutableLiveData<List<GetAllArticleResponse>> = MutableLiveData()
    val mutableCreateArticleLiveData: MutableLiveData<CreateArticleResponse> = MutableLiveData()

    fun startGetAllArticles() {
        viewModelScope.launch {
            val articles = articleRepository.getAll()
            mutableArticleLiveData.postValue(articles)

        }
    }



}