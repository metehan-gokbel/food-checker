package com.metehan.foodchecker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.metehan.foodchecker.models.FoodRecipe
import com.metehan.foodchecker.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}