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
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat


class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val differCallback = object: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = differ.currentList[position]

        holder.itemView.apply {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
            val output = formatter.format(parser.parse(news.publishedAt))

            findViewById<TextView>(R.id.tvNewsTitle).text = news.title
            findViewById<TextView>(R.id.tvNewsDescription).text = news.description
            findViewById<TextView>(R.id.tvNewsAuthor).text = news.author
            findViewById<TextView>(R.id.tvNewsPublishAt).text = output

            Glide.with(this)
                .load(news.urlToImage)
                .into(findViewById(R.id.ivNewsImage))

            // TODO: 24/11/2020 set on click listener
        }
    }

    override fun getItemCount() = differ.currentList.size
}