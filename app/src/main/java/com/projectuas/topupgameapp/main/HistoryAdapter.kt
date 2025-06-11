package com.projectuas.topupgameapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectuas.topupgameapp.databinding.ListItemRiwayatBinding
import com.projectuas.topupgameapp.data.model.HistoryModel

class HistoryAdapter(
    private val historyList: List<HistoryModel>
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

        // Tambahkan klik button "Pesan Lagi" jika dibutuhkan
        holder.binding.btnLogin.setOnClickListener {
            // TODO: Arahkan kembali ke halaman order jika ingin pesan lagi
            // Contoh:
            // val intent = Intent(it.context, OrderActivity::class.java)
            // it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = historyList.size
}
