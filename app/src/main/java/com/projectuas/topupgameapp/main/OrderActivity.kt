package com.projectuas.topupgameapp.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectuas.topupgameapp.R
import com.projectuas.topupgameapp.databinding.ActivityOrderBinding
import com.projectuas.topupgameapp.data.model.HistoryModel
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    private var jumlahPaket = IntArray(6) { 0 }
    private val hargaPaket = intArrayOf(10500, 15000, 23700, 22500, 16500, 26000)
    private val namaPaket = arrayOf(
        "Free Fire - 86 Diamonds",
        "PUBG Mobile - UC 300",
        "Mobile Legends - 140 Diamonds",
        "Valorant - VP 1000",
        "COD Mobile - CP 520",
        "Genshin - Genesis Crystal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        binding.btnCheckout.setOnClickListener {
            prosesPembayaran()
        }
    }

    private fun setupListeners() {
        setupButton(R.id.imageAdd1, R.id.imageMinus1, R.id.tvPaket1, 0)
        setupButton(R.id.imageAdd2, R.id.imageMinus2, R.id.tvPaket2, 1)
        setupButton(R.id.imageAdd3, R.id.imageMinus3, R.id.tvPaket3, 2)
        setupButton(R.id.imageAdd4, R.id.imageMinus4, R.id.tvPaket4, 3)
        setupButton(R.id.imageAdd5, R.id.imageMinus5, R.id.tvPaket5, 4)
        setupButton(R.id.imageAdd6, R.id.imageMinus6, R.id.tvPaket6, 5)
    }

    private fun setupButton(addId: Int, minusId: Int, textId: Int, index: Int) {
        val btnAdd = findViewById<ImageView>(addId)
        val btnMinus = findViewById<ImageView>(minusId)
        val tvJumlah = findViewById<TextView>(textId)

        btnAdd.setOnClickListener {
            jumlahPaket[index]++
            tvJumlah.text = jumlahPaket[index].toString()
            updateSummary()
        }

        btnMinus.setOnClickListener {
            if (jumlahPaket[index] > 0) {
                jumlahPaket[index]--
                tvJumlah.text = jumlahPaket[index].toString()
                updateSummary()
            }
        }
    }

    private fun updateSummary() {
        val totalItems = jumlahPaket.sum()
        val totalHarga = jumlahPaket.indices.sumOf { jumlahPaket[it] * hargaPaket[it] }

        binding.tvJumlahPorsi.text = "$totalItems items"
        binding.tvTotalPrice.text = "Rp $totalHarga"
    }

    private fun prosesPembayaran() {
        val totalItems = jumlahPaket.sum()
        val totalHarga = jumlahPaket.indices.sumOf { jumlahPaket[it] * hargaPaket[it] }

        if (totalItems == 0) {
            Toast.makeText(this, "Kamu belum memilih paket!", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val historyList: MutableList<HistoryModel> = getHistoryList()

        for (i in jumlahPaket.indices) {
            if (jumlahPaket[i] > 0) {
                val history = HistoryModel(
                    namaPaket = namaPaket[i],
                    jumlah = "${jumlahPaket[i]} item(s)",
                    harga = "Rp ${jumlahPaket[i] * hargaPaket[i]}",
                    status = "Top Up Berhasil Masuk",
                    tanggal = getCurrentDate()
                )
                historyList.add(history)
            }
        }

        val json = gson.toJson(historyList)
        editor.putString("history_list", json)
        editor.apply()

        Toast.makeText(
            this,
            "Yeay! Pesanan Anda sedang diproses, cek di menu riwayat ya!",
            Toast.LENGTH_LONG
        ).show()

        // Redirect ke MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getHistoryList(): MutableList<HistoryModel> {
        val sharedPreferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("history_list", null)
        val type = object : TypeToken<MutableList<HistoryModel>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
