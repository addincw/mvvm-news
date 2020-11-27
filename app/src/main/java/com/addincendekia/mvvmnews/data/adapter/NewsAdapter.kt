package com.addincendekia.mvvmnews.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.mvvmnews.R
import com.addincendekia.mvvmnews.data.model.Article
import com.addincendekia.mvvmnews.databinding.NewsItemBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat


class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var onItemClicked: ((Article) -> Unit)? = null
    private val differCallback = object: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = differ.currentList[position]
        val binding = NewsItemBinding.bind(holder.itemView)

        holder.itemView.apply {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
            val publishDate = formatter.format(parser.parse(news.publishedAt))

            binding.apply {
                tvNewsTitle.text = news.title
                tvNewsDescription.text = news.description
                tvNewsAuthor.text = news.author
                tvNewsPublishAt.text = publishDate
            }

            Glide.with(this)
                .load(news.urlToImage)
                .into(binding.ivNewsImage)

            setOnClickListener {
                onItemClicked?.let { it(news) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size
}