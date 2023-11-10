package com.metehan.foodchecker.util

class Constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "709fec5cb8414b43a991d19ecb5cfbdc"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        // Chip Preferences
        const val PREFERENCES_NAME = "food_checker_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"

        // Register and Login Preferences
        const val PREFERENCES_REMEMBER_ME = "rememberMe"
        const val PREFERENCES_EMAIL = "email"
        const val PREFERENCES_PASSWORD = "password"
        const val PREFERENCES_LOGGED_IN = "loggedIn"
    }
}