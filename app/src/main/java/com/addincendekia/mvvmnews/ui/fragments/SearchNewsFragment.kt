package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.adapter.NewsAdapter
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.databinding.FragmentSearchNewsBinding
import com.addincendekia.mvvmnews.ui.NewsActivity
import com.addincendekia.mvvmnews.util.Resource
import kotlinx.coroutines.*

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var searchNewsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)

        searchNewsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchNewsAdapter
        }

        viewModel = (activity as NewsActivity).viewModel
        viewModel.searchingNews().observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        searchNewsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    var toastMessage = "Failed get data: "

                    resource.data?.let {
                        toastMessage += it.message
                    }
                    resource.message?.let {
                        toastMessage += it
                    }

                    hideProgressBar()
                    Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> showProgressBar()
            }
        })

        var job: Job? = null
        binding.etSearchNews.addTextChangedListener{
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                if(it.toString().isNotEmpty()){
                    viewModel.getSearchingNews(it.toString())
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.pbSearchingNews.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbSearchingNews.visibility = View.INVISIBLE
    }
}