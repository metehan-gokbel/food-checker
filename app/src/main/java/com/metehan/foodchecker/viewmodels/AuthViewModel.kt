package com.metehan.foodchecker.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.metehan.foodchecker.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    val userLiveData = MutableLiveData<FirebaseUser>()
    val loggedOutLiveData = MutableLiveData<Boolean>()

    var readUser = dataStoreRepository.readUser.asLiveData()
    val readRememberMe = dataStoreRepository.readRememberMe.asLiveData()
    var readLoggedIn = dataStoreRepository.readLoggedIn.asLiveData()

    fun saveUser(email: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveUser(email, password)
        }

    fun saveRememberMe(rememberMe: Boolean) =
        viewModelScope.launch {
            dataStoreRepository.saveRememberMe(rememberMe)
        }

    fun saveLoggedIn(loggedIn: Boolean) =
        viewModelScope.launch {
            dataStoreRepository.saveLoggedIn(loggedIn)
        }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.firebase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        userLiveData.value = dataStoreRepository.firebase.currentUser
                    } else {
                        Toast.makeText(
                            getApplication(),
                            "Login Failure: " + it.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        }
    }


    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.firebase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        userLiveData.value = dataStoreRepository.firebase.currentUser
                    } else {
                        Toast.makeText(
                            getApplication(),
                            "Registration Failure: " + it.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        }
    }

    fun logOut() {
        dataStoreRepository.firebase.signOut()
        loggedOutLiveData.value = true
    }
}