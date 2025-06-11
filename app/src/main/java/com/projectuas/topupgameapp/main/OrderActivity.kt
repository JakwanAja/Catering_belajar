package com.projectuas.topupgameapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectuas.topupgameapp.R
import com.projectuas.topupgameapp.databinding.ActivityOrderBinding
import com.google.firebase.firestore.FirebaseFirestore

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
            val totalItems = jumlahPaket.sum()
            if (totalItems == 0) {
                Toast.makeText(this, "Kamu belum memilih paket!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PembayaranActivity::class.java)

                // Kirim data ke PembayaranActivity
                intent.putExtra("jumlahPaket", jumlahPaket)
                intent.putExtra("totalItems", totalItems)

                val totalHarga = jumlahPaket.indices.sumOf { jumlahPaket[it] * hargaPaket[it] }
                intent.putExtra("totalHarga", totalHarga)
                intent.putExtra("namaPaket", namaPaket)
                intent.putExtra("hargaPaket", hargaPaket)

                startActivity(intent)
            }
        }
    }

    // Function setup button berada di luar onCreate
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

    private fun setupListeners() {
        setupButton(R.id.imageAdd1, R.id.imageMinus1, R.id.tvPaket1, 0)
        setupButton(R.id.imageAdd2, R.id.imageMinus2, R.id.tvPaket2, 1)
        setupButton(R.id.imageAdd3, R.id.imageMinus3, R.id.tvPaket3, 2)
        setupButton(R.id.imageAdd4, R.id.imageMinus4, R.id.tvPaket4, 3)
        setupButton(R.id.imageAdd5, R.id.imageMinus5, R.id.tvPaket5, 4)
        setupButton(R.id.imageAdd6, R.id.imageMinus6, R.id.tvPaket6, 5)
    }

    private fun updateSummary() {
        val totalItems = jumlahPaket.sum()
        val totalHarga = jumlahPaket.indices.sumOf { jumlahPaket[it] * hargaPaket[it] }

        binding.tvJumlahPorsi.text = "$totalItems items"
        binding.tvTotalPrice.text = "Rp $totalHarga"
    }
}
//    private fun getHistoryList(): MutableList<HistoryModel> {
//        val sharedPreferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPreferences.getString("history_list", null)
//        val type = object : TypeToken<MutableList<HistoryModel>>() {}.type
//        return gson.fromJson(json, type) ?: mutableListOf()
//    }
//
//    private fun getCurrentDate(): String {
//        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
//        return sdf.format(Date())
//    }
//}
