package com.addincendekia.mvvmnews.data.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addincendekia.mvvmnews.NewsApplication
import com.addincendekia.mvvmnews.api.response.NewsResponse
import com.addincendekia.mvvmnews.data.model.Article
import com.addincendekia.mvvmnews.data.repository.NewsRepository
import com.addincendekia.mvvmnews.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    private val app: Application,
    private val newsRepository: NewsRepository
): AndroidViewModel(app) {
    private val breakingNews =  MutableLiveData<Resource<NewsResponse>>()
    private var breakingNewsCollection: NewsResponse? = null
    private var pageNumber = 1

    private val searchingNews =  MutableLiveData<Resource<NewsResponse>>()
    private var searchingPageNumber = 1

    fun breakingNews() = breakingNews
    fun breakingNewsPage() = pageNumber
    fun searchingNews() = searchingNews
    fun savedNews() = newsRepository.getSavedNews()

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, pageNumber)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error(null, "No Internet Connection"))
            }
        }catch (t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error(null, "Network Failure"))
                else -> breakingNews.postValue(Resource.Error(null, "Response Error"))
            }
        }

    }
    fun getSearchingNews(title: String) = viewModelScope.launch {
        searchingNews.postValue(Resource.Loading())

        val response = newsRepository.getSearchingNews(title, pageNumber)
        searchingNews.postValue(handleSearchingNewsResponse(response))
    }
    fun addSavedNews(article: Article) = viewModelScope.launch {
        newsRepository.addSavedNews(article)
    }
    fun deleteSavedNews(article: Article) = viewModelScope.launch {
        newsRepository.deleteSavedNews(article)
    }
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             val activeNetwork = connectivityManager.activeNetwork ?: return false
             val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        connectivityManager.activeNetworkInfo.run {
            return when(type){
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                TYPE_ETHERNET -> true
                else -> false
            }
        }
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                pageNumber++
                if (breakingNewsCollection != null){
                    val currentNewsList = breakingNewsCollection?.articles
                    currentNewsList?.addAll(it.articles)
                    return Resource.Success(breakingNewsCollection!!)
                }

                breakingNewsCollection = it
                return Resource.Success(it)
            }
        }

        response.errorBody()?.let {
            val respError = Gson().fromJson(it.string(), NewsResponse::class.java)
            return Resource.Error(respError, null)
        }

        return Resource.Error(null, response.message())
    }
    private fun handleSearchingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        response.errorBody()?.let {
            val respError = Gson().fromJson(it.string(), NewsResponse::class.java)
            return Resource.Error(respError, null)
        }

        return Resource.Error(null, response.message())
    }
}