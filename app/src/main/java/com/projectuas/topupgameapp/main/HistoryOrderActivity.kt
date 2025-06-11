package com.projectuas.topupgameapp.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projectuas.topupgameapp.data.model.HistoryModel
import com.projectuas.topupgameapp.databinding.ActivityHistoryOrderBinding

class HistoryOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryOrderBinding
    private lateinit var adapter: HistoryAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadHistoryFromFirestore()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(emptyList()) { historyItem ->
            showDeleteDialog(historyItem)
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
    }

    private fun loadHistoryFromFirestore() {
        if (uid == null) {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.rvHistory.visibility = View.GONE

        firestore.collection("users")
            .document(uid)
            .collection("history")
            .get()
            .addOnSuccessListener { result ->
                val historyList = result.map { doc ->
                    HistoryModel(
                        namaPaket = doc.getString("namaPaket") ?: "",
                        jumlah = doc.getString("jumlah") ?: "",
                        harga = doc.getString("harga") ?: "",
                        status = doc.getString("status") ?: "",
                        tanggal = doc.getString("tanggal") ?: ""
                    )
                }

                binding.progressBar.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE

                if (historyList.isEmpty()) {
                    binding.tvNotFound.visibility = View.VISIBLE
                } else {
                    binding.tvNotFound.visibility = View.GONE
                    adapter = HistoryAdapter(historyList) { item ->
                        showDeleteDialog(item)
                    }
                    binding.rvHistory.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat riwayat", Toast.LENGTH_SHORT).show()
                binding.tvNotFound.visibility = View.VISIBLE
            }
    }

    private fun showDeleteDialog(item: HistoryModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Riwayat")
        builder.setMessage("Yakin ingin menghapus \"${item.namaPaket}\" dari riwayat?")
        builder.setPositiveButton("Hapus") { _, _ ->
            deleteHistoryFromFirestore(item)
        }
        builder.setNegativeButton("Batal", null)
        builder.show()
    }

    private fun deleteHistoryFromFirestore(item: HistoryModel) {
        if (uid == null) return

        firestore.collection("users")
            .document(uid)
            .collection("history")
            .whereEqualTo("namaPaket", item.namaPaket)
            .whereEqualTo("tanggal", item.tanggal) // Gunakan kombinasi unik
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    firestore.collection("users")
                        .document(uid)
                        .collection("history")
                        .document(doc.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Riwayat dihapus", Toast.LENGTH_SHORT).show()
                            loadHistoryFromFirestore()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Terjadi kesalahan saat mencari data", Toast.LENGTH_SHORT).show()
            }
    }
}
