package com.metehan.foodchecker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.metehan.foodchecker.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var loggedIn = false

    val readUserData = dataStoreRepository.readUserData
    var readLoggedIn = dataStoreRepository.readLoggedIn.asLiveData()

    fun saveLoggedIn(loggedIn: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveLoggedIn(loggedIn)
        }
    }

    fun saveUserData(email: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveUserData(email, password)
        }
}