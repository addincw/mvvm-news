package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.databinding.FragmentDetailNewsBinding
import com.addincendekia.mvvmnews.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class DetailNewsFragment : Fragment(R.layout.fragment_detail_news){
    private val args: DetailNewsFragmentArgs by navArgs()

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentDetailNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding = FragmentDetailNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel

        binding.wvDetailNews.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        binding.fabSavedNews.setOnClickListener {
            viewModel.addSavedNews(article)
            Snackbar.make(view, "article saved", Snackbar.LENGTH_LONG).show()
        }
    }
}