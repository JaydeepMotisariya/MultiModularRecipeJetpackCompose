package com.riseup.search.domain.repository

import com.riseup.search.domain.model.Recipe
import com.riseup.search.domain.model.RecipeDetails

interface SearchRepository {
    suspend fun getRecipes(s:String): Result<List<Recipe>>
    suspend fun getRecipeDetails(id: String): Result<RecipeDetails>
}