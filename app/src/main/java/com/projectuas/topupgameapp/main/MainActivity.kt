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

        setupCategories()
        setupTrending()
        setupLogout()
    }

    private fun setupCategories() {
        val categories = listOf(
            ModelCategories(R.drawable.pubg, "PUBG Mobile"),
            ModelCategories(R.drawable.freefire, "Free Fire"),
            ModelCategories(R.drawable.valorant, "Valorant"),
            ModelCategories(R.drawable.mobilelegend, "Mobile Legends"),
            ModelCategories(R.drawable.ghensin, "Genshin Impact")
        )

        categoriesAdapter = CategoriesAdapter(categories)
        binding.rvCategories.layoutManager = GridLayoutManager(this, 3)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun setupTrending() {
        val trendingList = listOf(
            ModelTrending(R.drawable.complete_1, "MLBB 86 Diamonds", "1.200 disukai"),
            ModelTrending(R.drawable.complete_1, "PUBG UC 300", "1.500 disukai"),
            ModelTrending(R.drawable.complete_1, "FF 70 Diamonds", "900 disukai"),
            ModelTrending(R.drawable.complete_1, "Valorant VP 1.000", "2.000 disukai")
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
