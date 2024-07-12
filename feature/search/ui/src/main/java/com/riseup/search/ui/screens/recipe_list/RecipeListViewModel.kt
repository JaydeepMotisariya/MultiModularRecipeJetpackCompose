package com.riseup.search.ui.screens.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riseup.common.utils.NetworkResult
import com.riseup.common.utils.UiText
import com.riseup.search.domain.model.Recipe
import com.riseup.search.domain.use_cases.GetAllRecipeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    private val getAllRecipesUseCase: GetAllRecipeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeList.UiState())
    val uiState: StateFlow<RecipeList.UiState> get() = _uiState.asStateFlow()

    fun onEvent(event: RecipeList.Event) {
        when (event) {
            is RecipeList.Event.SearchRecipe -> {
                searchRecipe(event.q)
            }

            RecipeList.Event.FavoriteScreen -> TODO()
            is RecipeList.Event.GoToRecipeDetails -> TODO()
        }
    }

    private fun searchRecipe(q: String) =
        getAllRecipesUseCase.invoke(q).onEach { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    _uiState.update {
                        RecipeList.UiState(isLoading = true)
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        RecipeList.UiState(error = UiText.RemoteString(result.message.toString()))
                    }
                }

                is NetworkResult.Success -> {
                    _uiState.update {
                        RecipeList.UiState(data = result.data)
                    }
                }
            }
        }.launchIn(viewModelScope)

    object RecipeList {

        data class UiState(
            val isLoading: Boolean = false,
            val error: UiText = UiText.Idle,
            val data: List<Recipe>? = null
        )

        sealed interface Navigation {

            data class GoToRecipeDetails(val id: String) : Navigation

            data object GoToFavoriteScreen : Navigation

        }

        sealed interface Event {
            data class SearchRecipe(val q: String) : Event

            data class GoToRecipeDetails(val id: String) : Event

            data object FavoriteScreen : Event
        }

    }
}