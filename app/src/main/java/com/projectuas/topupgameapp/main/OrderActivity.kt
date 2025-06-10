package com.projectuas.topupgameapp.main

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.projectuas.topupgameapp.R
import com.projectuas.topupgameapp.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    private var jumlahPaket = IntArray(6) { 0 }
    private val hargaPaket = intArrayOf(10500, 15000, 23700, 22500, 16500, 26000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        setupButton(
            addId = R.id.imageAdd1, minusId = R.id.imageMinus1, textId = R.id.tvPaket1, index = 0
        )
        setupButton(
            addId = R.id.imageAdd2, minusId = R.id.imageMinus2, textId = R.id.tvPaket2, index = 1
        )
        setupButton(
            addId = R.id.imageAdd3, minusId = R.id.imageMinus3, textId = R.id.tvPaket3, index = 2
        )
        setupButton(
            addId = R.id.imageAdd4, minusId = R.id.imageMinus4, textId = R.id.tvPaket4, index = 3
        )
        setupButton(
            addId = R.id.imageAdd5, minusId = R.id.imageMinus5, textId = R.id.tvPaket5, index = 4
        )
        setupButton(
            addId = R.id.imageAdd6, minusId = R.id.imageMinus6, textId = R.id.tvPaket6, index = 5
        )
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
}
