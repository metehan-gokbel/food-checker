package com.metehan.foodchecker.data

import android.content.Context
import android.health.connect.datatypes.MealType
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.metehan.foodchecker.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.metehan.foodchecker.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.metehan.foodchecker.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// Bu class daha sonra RecipesViewModel içerisinde kullanılacağı için @ActivityRetainedScoped ekledik.
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

     private object PreferencesKeys{
         val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
         val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
         val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
         val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
     }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int){
        dataStore.edit {preferences->
            preferences[PreferencesKeys.selectedMealType] = mealType
            preferences[PreferencesKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKeys.selectedDietType] = dietType
            preferences[PreferencesKeys.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences->
            // Eğer herhangi bir değer kaydedilmemişse main course'u emit et.
            val selectedMealType = preferences[PreferencesKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            // Eğer herhangi bir değer kaydedilmemişse 0'ı emit et.
            val selectedMealTypeId = preferences[PreferencesKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferencesKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferencesKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)