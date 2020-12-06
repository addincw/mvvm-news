package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
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
import com.addincendekia.mvvmnews.util.Constant.NEWS.API_ITEM_PER_PAGE
import com.addincendekia.mvvmnews.util.Resource
import com.google.android.material.snackbar.Snackbar

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentBreakingNewsBinding

    private val newsAdapter = NewsAdapter()

    private var rvIsLoading = false
    private var rvIsScrolling = false
    private var rvIsLastPage = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        viewModel.getBreakingNews("us")

        setupRvBreakingNews()

        setupDataObserver()
    }
    private fun setupDataObserver() {
        viewModel.breakingNews().observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        if((it.totalResults / API_ITEM_PER_PAGE + 2) == viewModel.breakingNewsPage()){
                            rvIsLastPage = true
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
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
    private fun setupRvBreakingNews() {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                rvIsScrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.rvBreakingNews.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val totalVisibleItem = layoutManager.childCount
                val totalFetchItem = layoutManager.itemCount

                val isAtBeginningItem = firstVisibleItemPosition == 0
                val isAtLastItemFetch = firstVisibleItemPosition + totalVisibleItem >= totalFetchItem
                val isTotalFetchItemMtPerPageItem = totalFetchItem >= API_ITEM_PER_PAGE
                val isShouldPaginate = rvIsScrolling && !rvIsLoading && !rvIsLastPage
                        && !isAtBeginningItem && isAtLastItemFetch && isTotalFetchItemMtPerPageItem

                if(isShouldPaginate){
                    viewModel.getBreakingNews("us")
                    rvIsScrolling = false
                }
            }
        }

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply { putSerializable("article", article) }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_detailNewsFragment, bundle)
        }
        binding.rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
            addOnScrollListener(onScrollListener)
        }
    }
    private fun hideProgressBar() {
        binding.pbBreakingNews.visibility = View.INVISIBLE
        rvIsLoading = false
    }
    private fun showProgressBar() {
        binding.pbBreakingNews.visibility = View.VISIBLE
        rvIsLoading = true
    }
}
