package com.metehan.foodchecker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.metehan.foodchecker.models.Result
import com.metehan.foodchecker.util.Constants.Companion.FAVOURITE_RECIPES_TABLE

@Entity(tableName = FAVOURITE_RECIPES_TABLE)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)