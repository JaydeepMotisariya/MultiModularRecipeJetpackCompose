package com.riseup.search.data.repository

import com.riseup.search.data.mappers.toDomain
import com.riseup.search.data.remote.SearchApiService
import com.riseup.search.domain.model.Recipe
import com.riseup.search.domain.model.RecipeDetails
import com.riseup.search.domain.repository.SearchRepository

class SearchRepoIml(
    private val searchApiService: SearchApiService
) : SearchRepository {
    override suspend fun getRecipes(s: String): Result<List<Recipe>> {
        return try {
            val response = searchApiService.getRecipes(s)
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.toDomain())
                } ?: run { Result.failure(Exception("Error occurred on recipe fetching")) }
            } else {
                Result.failure(Exception("Error occurred on recipe fetching"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun getRecipeDetails(id: String): Result<RecipeDetails> {
        return try {
            val response = searchApiService.getRecipeDetails(id)
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    if (it.isNotEmpty()) {
                        Result.success(it.first().toDomain())
                    } else {
                        Result.failure(Exception("Error occurred on recipe details fetching"))
                    }
                } ?: run {
                    Result.failure(Exception("Error occurred on recipe details fetching"))
                }
            } else {
                Result.failure(Exception("Error occurred on recipe details fetching"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }


    }
}