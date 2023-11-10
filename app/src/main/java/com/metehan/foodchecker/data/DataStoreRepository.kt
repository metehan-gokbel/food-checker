package com.metehan.foodchecker.data

import android.content.Context
import android.health.connect.datatypes.MealType
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.google.firebase.auth.FirebaseAuth
import com.metehan.foodchecker.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.metehan.foodchecker.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_EMAIL
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_LOGGED_IN
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_NAME
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_PASSWORD
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_REMEMBER_ME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// Bu class daha sonra RecipesViewModel içerisinde kullanılacağı için @ActivityRetainedScoped ekledik.
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    firebaseAuth: FirebaseAuth
) {

    val firebase = firebaseAuth

    private object PreferencesKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
        val rememberMe = preferencesKey<Boolean>(PREFERENCES_REMEMBER_ME)
        val email = preferencesKey<String>(PREFERENCES_EMAIL)
        val password = preferencesKey<String>(PREFERENCES_PASSWORD)
        val loggedIn = preferencesKey<Boolean>(PREFERENCES_LOGGED_IN)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedMealType] = mealType
            preferences[PreferencesKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKeys.selectedDietType] = dietType
            preferences[PreferencesKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    suspend fun saveLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.loggedIn] = loggedIn
        }
    }

    suspend fun saveRememberMe(rememberMe: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.rememberMe] = rememberMe
        }
    }

    suspend fun saveUser(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.email] = email
            preferences[PreferencesKeys.password] = password
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Eğer herhangi bir değer kaydedilmemişse main course'u emit et.
            val selectedMealType =
                preferences[PreferencesKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            // Eğer herhangi bir değer kaydedilmemişse 0'ı emit et.
            val selectedMealTypeId = preferences[PreferencesKeys.selectedMealTypeId] ?: 0
            val selectedDietType =
                preferences[PreferencesKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferencesKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline
        }

    val readRememberMe: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val rememberMe = preferences[PreferencesKeys.rememberMe] ?: false
            rememberMe
        }

    val readLoggedIn: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val loggedIn = preferences[PreferencesKeys.loggedIn] ?: false
            loggedIn
        }

    val readUser: Flow<User> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val email = preferences[PreferencesKeys.email] ?: ""
            val password = preferences[PreferencesKeys.password] ?: ""
            User(email, password)
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)

data class User(
    val email: String,
    val password: String
)
