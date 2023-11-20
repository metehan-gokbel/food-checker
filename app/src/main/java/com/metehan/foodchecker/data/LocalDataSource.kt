package com.metehan.foodchecker.data

import com.metehan.foodchecker.data.database.RecipesDao
import com.metehan.foodchecker.data.database.entities.FavouritesEntity
import com.metehan.foodchecker.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    // RECIPE
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }


    // FAVOURITE RECIPE
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>> {
        return recipesDao.readFavouriteRecipes()
    }

    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity) {
        recipesDao.insertFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        recipesDao.deleteFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouriteRecipes() {
        recipesDao.deleteAllFavouriteRecipes()
    }
}