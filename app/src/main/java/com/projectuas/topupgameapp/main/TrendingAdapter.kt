package com.projectuas.topupgameapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectuas.topupgameapp.databinding.ListItemTrendingBinding

class TrendingAdapter(
    private val trendingList: List<ModelTrending>
) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trending = trendingList[position]
        holder.binding.imgThumb.setImageResource(trending.image)
        holder.binding.tvPlaceName.text = trending.title
        holder.binding.tvVote.text = trending.liked
    }

    override fun getItemCount(): Int = trendingList.size
}
