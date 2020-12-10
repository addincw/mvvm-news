package com.addincendekia.mvvmnews.data.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.addincendekia.mvvmnews.NewsApplication
import com.addincendekia.mvvmnews.data.repository.NewsRepository

class NewsViewModelFactory(
        private val app: Application,
        private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}