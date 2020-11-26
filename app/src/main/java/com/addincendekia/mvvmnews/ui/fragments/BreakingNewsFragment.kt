package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.adapter.NewsAdapter
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.databinding.FragmentBreakingNewsBinding
import com.addincendekia.mvvmnews.ui.NewsActivity
import com.addincendekia.mvvmnews.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel
        viewModel.getBreakingNews("us")

        val newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }

        viewModel.breakingNews().observe(viewLifecycleOwner, Observer { resource ->
            when(resource){
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let {
                        Toast.makeText(activity, "failed get data: $it", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        })
    }

    private fun hideProgressBar() {
        binding.pbBreakingNews.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.pbBreakingNews.visibility = View.VISIBLE
    }
}
