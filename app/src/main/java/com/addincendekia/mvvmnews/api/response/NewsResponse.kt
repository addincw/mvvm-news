package com.addincendekia.mvvmnews.api.response

import com.addincendekia.mvvmnews.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)