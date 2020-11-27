package com.addincendekia.mvvmnews.data.repository

import com.addincendekia.mvvmnews.api.ApiInstance
import com.addincendekia.mvvmnews.data.model.Article
import com.addincendekia.mvvmnews.db.NewsDatabase

class NewsRepository(private val db: NewsDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = ApiInstance.api.getNews(countryCode, pageNumber)
    suspend fun getSearchingNews(title: String, pageNumber: Int) = ApiInstance.api.getFilteredNews(title, pageNumber)

    suspend fun addSavedNews(article: Article) = db.getArticleDao().insertOrUpdate(article)
    suspend fun deleteSavedNews(article: Article) = db.getArticleDao().delete(article)
    fun getSavedNews() = db.getArticleDao().all()
}
