package com.addincendekia.mvvmnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.repository.NewsRepository
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModelFactory
import com.addincendekia.mvvmnews.db.NewsDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactory).get(NewsViewModel::class.java)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val newsHostFragment = supportFragmentManager.findFragmentById(R.id.newsHostFragment)
        bottomNavigationView.setupWithNavController(newsHostFragment!!.findNavController())
    }
}