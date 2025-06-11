package com.projectuas.topupgameapp.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectuas.topupgameapp.R
import com.projectuas.topupgameapp.data.model.HistoryModel
import com.projectuas.topupgameapp.databinding.ListItemRiwayatBinding

class HistoryAdapter(
    private val historyList: List<HistoryModel>,
    private val onLongClick: (HistoryModel) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemRiwayatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyList[position]

        holder.binding.tvNama.text = history.namaPaket
        holder.binding.tvPrice.text = history.harga
        holder.binding.tvJml.text = history.jumlah
        holder.binding.tvStatus.text = history.status
        holder.binding.tvDate.text = history.tanggal

        // Ganti logo sesuai nama game
        val logoResId = when {
            history.namaPaket.contains("Free Fire", true) -> R.drawable.freefire_logo
            history.namaPaket.contains("PUBG", true) -> R.drawable.pubg_logo
            history.namaPaket.contains("Mobile Legends", true) -> R.drawable.mobile_legend_logo
            history.namaPaket.contains("Valorant", true) -> R.drawable.valorant_logo
            history.namaPaket.contains("Genshin", true) -> R.drawable.ghensin_impact_logo
            else -> R.drawable.gamepad
        }
        holder.binding.imgLogo.setImageResource(logoResId)

        // Klik tombol "Pesan Lagi"
        holder.binding.btnLogin.setOnClickListener {
            val intent = Intent(it.context, OrderActivity::class.java)
            it.context.startActivity(intent)
        }

        // Long click untuk menghapus item
        holder.itemView.setOnLongClickListener {
            onLongClick(history)
            true
        }
    }

    override fun getItemCount(): Int = historyList.size
}
