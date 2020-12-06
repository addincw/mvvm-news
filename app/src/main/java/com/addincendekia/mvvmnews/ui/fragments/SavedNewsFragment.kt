package com.addincendekia.mvvmnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.adapter.NewsAdapter
import com.addincendekia.mvvmnews.data.viewmodel.NewsViewModel
import com.addincendekia.mvvmnews.databinding.FragmentSavedNewsBinding
import com.addincendekia.mvvmnews.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment: Fragment(R.layout.fragment_saved_news){
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        var savedNewsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = savedNewsAdapter
        }

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                val item = savedNewsAdapter.differ.currentList[itemPosition]

                viewModel.deleteSavedNews(item)
                Snackbar.make(view, "deleted news", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.addSavedNews(item)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.savedNews().observe(viewLifecycleOwner, Observer {
            savedNewsAdapter.differ.submitList(it)
        })
    }
}