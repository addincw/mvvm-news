package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.adapter.NewsAdapter
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.databinding.FragmentBreakingNewsBinding
import com.addincendekia.mvvmnews.ui.NewsActivity
import com.addincendekia.mvvmnews.util.Resource
import com.google.android.material.snackbar.Snackbar

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel
        viewModel.getBreakingNews("us")

        val newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply { putSerializable("article", article) }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_detailNewsFragment, bundle)
        }
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
    }

    private fun hideProgressBar() {
        binding.pbBreakingNews.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.pbBreakingNews.visibility = View.VISIBLE
    }
}
