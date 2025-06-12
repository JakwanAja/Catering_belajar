package com.projectuas.topupgameapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.projectuas.topupgameapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class PembayaranActivity : AppCompatActivity() {

    private lateinit var jumlahPaket: IntArray
    private lateinit var namaPaket: Array<String>
    private lateinit var hargaPaket: IntArray
    private var totalItems = 0
    private var totalHarga = 0

    private lateinit var tvNamaPaket: TextView
    private lateinit var tvHargaPaket: TextView
    private lateinit var tvTotalHarga: TextView
    private lateinit var buttonBeli: Button

    private var metodePembayaran: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pembayaran)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvNamaPaket = findViewById(R.id.tvNamaPaket)
        tvHargaPaket = findViewById(R.id.tvHargaPaket)
        tvTotalHarga = findViewById(R.id.tvTotalHarga)
        buttonBeli = findViewById(R.id.buttonBeli)

        val btnBCA = findViewById<ImageButton>(R.id.imageButton_BCA)
        val btnGOPAY = findViewById<ImageButton>(R.id.imageButton_GOPAY)
        val btnDANA = findViewById<ImageButton>(R.id.imageButton_DANA)

        jumlahPaket = intent.getIntArrayExtra("jumlahPaket")!!
        namaPaket = intent.getStringArrayExtra("namaPaket")!!
        hargaPaket = intent.getIntArrayExtra("hargaPaket")!!
        totalItems = intent.getIntExtra("totalItems", 0)
        totalHarga = intent.getIntExtra("totalHarga", 0)

        tampilkanData()

        btnBCA.setOnClickListener {
            metodePembayaran = "BCA"
            Toast.makeText(this, "Metode Pembayaran: BCA", Toast.LENGTH_SHORT).show()
        }

        btnGOPAY.setOnClickListener {
            metodePembayaran = "GOPAY"
            Toast.makeText(this, "Metode Pembayaran: GOPAY", Toast.LENGTH_SHORT).show()
        }

        btnDANA.setOnClickListener {
            metodePembayaran = "DANA"
            Toast.makeText(this, "Metode Pembayaran: DANA", Toast.LENGTH_SHORT).show()
        }

        buttonBeli.setOnClickListener {
            if (metodePembayaran == null) {
                Toast.makeText(this, "Silakan pilih metode pembayaran!", Toast.LENGTH_SHORT).show()
            } else {
                prosesPembayaran()
            }
        }

    }

    private fun tampilkanData() {
        for (i in jumlahPaket.indices) {
            if (jumlahPaket[i] > 0) {
                tvNamaPaket.text = namaPaket[i]
                tvHargaPaket.text = "Rp ${jumlahPaket[i] * hargaPaket[i]}"
                break
            }
        }

        tvTotalHarga.text = "Rp $totalHarga"
    }

    private fun prosesPembayaran() {
        if (totalItems == 0) {
            Toast.makeText(this, "Tidak ada item yang diproses!", Toast.LENGTH_SHORT).show()
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
                    "metodePembayaran" to metodePembayaran,
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

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}


