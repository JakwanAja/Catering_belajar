package com.projectuas.topupgameapp.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.projectuas.topupgameapp.R
import com.projectuas.topupgameapp.auth.LoginActivity
import com.projectuas.topupgameapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var trendingAdapter: TrendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSource.setOnClickListener {
            startActivity(Intent(this, HistoryOrderActivity::class.java))
        }

        setupCategories()
        setupTrending()
        setupLogout()
    }

// Setup isi bagian "Game Categories"
    private fun setupCategories() {
        val categories = listOf(
            ModelCategories(R.drawable.pubg_logo, "PUBG Mobile"),
            ModelCategories(R.drawable.freefire_logo, "Free Fire"),
            ModelCategories(R.drawable.valorant_logo, "Valorant"),
            ModelCategories(R.drawable.mobile_legend_logo, "Mobile Legends"),
            ModelCategories(R.drawable.ghensin_impact_logo, "Genshin Impact"),
            ModelCategories(R.drawable.gamepad, "Others")
        )

        categoriesAdapter = CategoriesAdapter(categories)
        binding.rvCategories.layoutManager = GridLayoutManager(this, 3)
        binding.rvCategories.adapter = categoriesAdapter
    }

// Setup isi bagian "New & Popular TopUps"
    private fun setupTrending() {
        val trendingList = listOf(
            ModelTrending(R.drawable.popular_2, "PUBG UC 300", "1.500 disukai"),
            ModelTrending(R.drawable.popular_1, "MLBB 86 Diamonds", "1.200 disukai"),
            ModelTrending(R.drawable.popular_3, "FF 70 Diamonds", "900 disukai"),
            ModelTrending(R.drawable.popular_4, "Valorant VP 1.000", "2.000 disukai")
        )

        trendingAdapter = TrendingAdapter(trendingList)
        binding.rvTrending.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTrending.adapter = trendingAdapter
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
