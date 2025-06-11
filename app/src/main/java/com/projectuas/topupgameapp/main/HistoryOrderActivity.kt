package com.projectuas.topupgameapp.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectuas.topupgameapp.databinding.ActivityHistoryOrderBinding
import com.projectuas.topupgameapp.data.model.HistoryModel

class HistoryOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryOrderBinding
    private lateinit var adapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadDummyHistory() // replace this later with Firebase or Room DB
    }

    private fun setupRecyclerView() {
        val list = getHistoryList() // Ambil dari SharedPreferences
        adapter = HistoryAdapter(list)
        binding.tvNotFound.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun getHistoryList(): MutableList<HistoryModel> {
        val sharedPreferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("history_list", null)
        val type = object : TypeToken<MutableList<HistoryModel>>() {}.type

        return if (json != null) {
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
    private fun loadDummyHistory() {
        historyList.add(
            HistoryModel(
                namaPaket = "MLBB 86 Diamonds",
                harga = "Rp 10.500",
                jumlah = "1 item",
                status = "Top Up Berhasil Masuk",
                tanggal = "11 Juni 2025"
            )
        )
        adapter.notifyDataSetChanged()
    }
}
