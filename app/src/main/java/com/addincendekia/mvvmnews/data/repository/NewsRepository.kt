package com.addincendekia.mvvmnews.data.repository

import com.addincendekia.mvvmnews.api.ApiInstance
import com.addincendekia.mvvmnews.db.NewsDatabase

class NewsRepository(db: NewsDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = ApiInstance.api.getNews(countryCode, pageNumber)
}
