package com.metehan.foodchecker.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.metehan.foodchecker.R
import com.metehan.foodchecker.databinding.ActivityLoginBinding
import com.metehan.foodchecker.databinding.ActivitySignUpBinding
import com.metehan.foodchecker.ui.MainActivity
import com.metehan.foodchecker.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.lifecycleOwner = this
        binding.authViewModel = authViewModel

        authViewModel.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(email, password)
            } else {
                Toast.makeText(this@LoginActivity, "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.signRedirectText.setOnClickListener {
            val signUpIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}