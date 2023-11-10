package com.metehan.foodchecker.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.metehan.foodchecker.R
import com.metehan.foodchecker.databinding.ActivityLoginBinding
import com.metehan.foodchecker.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.ivNote.alpha = 0f
        binding.ivNote.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}