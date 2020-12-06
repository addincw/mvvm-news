package com.addincendekia.mvvmnews.api.response

import com.addincendekia.mvvmnews.data.model.Article

data class NewsResponse(
    val status: String,
    val code: String?,
    val message: String?,
    val articles: MutableList<Article>,
    val totalResults: Int,
)