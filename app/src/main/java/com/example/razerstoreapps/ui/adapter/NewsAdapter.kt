package com.example.razerstoreapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.model.Article
import com.example.razerstoreapps.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(private val articles: List<Article>, private val onItemClick: (Article) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvNewsTitle.text = article.title
            binding.tvNewsSummary.text = article.description ?: article.content ?: ""
            
            // Format date if possible
            val formattedDate = formatPublishDate(article.publishedAt)
            binding.tvPublishDate.text = formattedDate

            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(binding.imgThumbnail)

            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }

        private fun formatPublishDate(rawDate: String?): String {
            if (rawDate == null) return "-"
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                val date = inputFormat.parse(rawDate)
                if (date != null) outputFormat.format(date) else rawDate
            } catch (e: Exception) {
                rawDate
            }
        }
    }
}
