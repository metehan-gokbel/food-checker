package com.metehan.foodchecker.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.metehan.foodchecker.R
import com.metehan.foodchecker.databinding.ActivitySignUpBinding
import com.metehan.foodchecker.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    //    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.lifecycleOwner = this
        binding.authViewModel = authViewModel

        authViewModel.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

//        firebaseAuth = FirebaseAuth.getInstance()
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    authViewModel.register(email, password)
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Password does not matched.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@SignUpActivity, "Fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}