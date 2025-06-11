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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityOrderBinding
    private var jumlahPaket = IntArray(6) { 0 }
    private val hargaPaket = intArrayOf(9000, 15000, 25700, 59900, 109500, 210000)
    private val namaPaket = arrayOf(
        "PUBG Mobile - UC 60",
        "PUBG Mobile - UC 100",
        "PUBG Mobile - UC 170",
        "PUBG Mobile - UC 500",
        "PUBG Mobile - UC 1050",
        "PUBG Mobile - UC 2200"
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

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in jumlahPaket.indices) {
            if (jumlahPaket[i] > 0) {
                val historyData = hashMapOf(
                    "namaPaket" to namaPaket[i],
                    "jumlah" to "${jumlahPaket[i]} item(s)",
                    "harga" to "Rp ${jumlahPaket[i] * hargaPaket[i]}",
                    "status" to "Top Up Berhasil Masuk",
                    "tanggal" to getCurrentDate()
                )

                db.collection("users")
                    .document(userId)
                    .collection("history")
                    .add(historyData)
            }
        }

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
