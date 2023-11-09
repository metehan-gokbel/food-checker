package com.metehan.foodchecker.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.metehan.foodchecker.data.DataStoreRepository
import com.metehan.foodchecker.data.Repository
import com.metehan.foodchecker.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    val userLiveData = MutableLiveData<FirebaseUser>()
    val loggedOutLiveData = MutableLiveData<Boolean>()

    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.firebase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        userLiveData.value = repository.firebase.currentUser
                    }else{
                        Toast.makeText(getApplication(), "Login Failure: " + it.exception?.message.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    fun register(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.firebase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        userLiveData.value = repository.firebase.currentUser
                    }else{
                        Toast.makeText(getApplication(), "Registration Failure: " + it.exception?.message.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    fun logOut(){
        repository.firebase.signOut()
        loggedOutLiveData.value = true
    }
}