package com.metehan.foodchecker.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
    private var redirectToMain = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.lifecycleOwner = this
        binding.authViewModel = authViewModel

        redirectToMain = false

        authViewModel.readRememberMe.observe(this){
            if(it && !redirectToMain){
                binding = ActivityLoginBinding.inflate(layoutInflater)
                setContentView(binding.root)
                authViewModel.readUser.observe(this){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    redirectToMain = true
                }
            }
        }

        authViewModel.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null && !redirectToMain) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            val rememberMe = binding.rememberMe.isChecked

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(email, password)
                if(rememberMe){
                    authViewModel.saveUser(email, password)
                    authViewModel.saveRememberMe(true)
                }
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