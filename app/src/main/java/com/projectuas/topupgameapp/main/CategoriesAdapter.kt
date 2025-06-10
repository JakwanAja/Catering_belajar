package com.projectuas.topupgameapp.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectuas.topupgameapp.databinding.ListItemCategoriesBinding

class CategoriesAdapter(
    private val categoriesList: List<ModelCategories>
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCategoriesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.binding.imageIcon.setImageResource(category.image)
        holder.binding.tvName.text = category.name

        // ⏬ Tambahkan fungsi klik
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OrderActivity::class.java).apply {
                putExtra("CATEGORY_NAME", category.name)
                putExtra("CATEGORY_IMAGE", category.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categoriesList.size
}
