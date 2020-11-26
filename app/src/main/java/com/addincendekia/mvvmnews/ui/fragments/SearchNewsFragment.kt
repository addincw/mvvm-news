package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.ui.NewsActivity

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
    }
}