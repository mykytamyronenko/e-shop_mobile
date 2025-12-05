package com.school.projettm

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.school.projettm.placeholder.PlaceholderContent.PlaceholderItem
import com.school.projettm.databinding.FragmentArticleListItemBinding
import com.school.projettm.dtos.GetAllArticleResponse
import com.school.projettm.models.Article

class ArticleRecyclerViewAdapter(
    private val values: List<Article>,
    private var callback: ArticleRecyclerViewAdapterCallback
) : RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>() {

    interface ArticleRecyclerViewAdapterCallback {
        fun onArticleClick(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentArticleListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = values[position]
        holder.tvTitle.text = article.title
        holder.priceView.text = "${article.price}€"
        holder.categoryView.text = article.category
        holder.stateView.text = article.state
        holder.quantityView.text = article.quantity.toString()

        holder.tvTitle.setOnClickListener{
            callback.onArticleClick(article)
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle: TextView = binding.tvArticleItemFragmentTitle
        val priceView: TextView = binding.tvPrice
        val categoryView: TextView = binding.tvCategory
        val stateView: TextView = binding.tvState
        val quantityView: TextView = binding.tvQuantity
    }

}