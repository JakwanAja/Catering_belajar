package com.projectuas.topupgameapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.projectuas.topupgameapp.auth.LoginActivity
import com.projectuas.topupgameapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Efek fade out
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            duration = 800
            startOffset = 1200
            fillAfter = true
        }

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        binding.imgSplashLogo.startAnimation(fadeOut)
    }
}
