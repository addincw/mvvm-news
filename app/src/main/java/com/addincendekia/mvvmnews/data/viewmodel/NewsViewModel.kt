package com.addincendekia.mvvmnews.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addincendekia.mvvmnews.api.response.NewsResponse
import com.addincendekia.mvvmnews.data.repository.NewsRepository
import com.addincendekia.mvvmnews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {
    private val breakingNews =  MutableLiveData<Resource<NewsResponse>>()
    private val pageNumber = 1

    fun breakingNews() = breakingNews

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        val response = newsRepository.getBreakingNews(countryCode, pageNumber)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(null, response.message())
    }
}