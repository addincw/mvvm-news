package com.addincendekia.mvvmnews.api.request

import com.addincendekia.mvvmnews.api.response.NewsResponse
import com.addincendekia.mvvmnews.util.Constant.Companion.NEWS_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsRequest {
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = NEWS_API_KEY,
    ): Response<NewsResponse>
    @GET("v2/everything")
    suspend fun getFilteredNews(
        @Query("q")
        title: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = NEWS_API_KEY,
    ): Response<NewsResponse>
}