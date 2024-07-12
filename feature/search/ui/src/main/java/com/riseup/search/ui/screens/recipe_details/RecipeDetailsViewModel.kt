package com.riseup.search.ui.screens.recipe_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riseup.common.utils.NetworkResult
import com.riseup.common.utils.UiText
import com.riseup.search.domain.model.RecipeDetails
import com.riseup.search.domain.use_cases.GetRecipeDetailsUseCase
import com.riseup.search.ui.screens.recipe_details.RecipeDetails.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    fun onEvent(event: com.riseup.search.ui.screens.recipe_details.RecipeDetails.Event) {
        when (event) {
            is com.riseup.search.ui.screens.recipe_details.RecipeDetails.Event.FetchRecipeDetails -> {
                recipeDetails(event.id)
            }
        }
    }

    private fun recipeDetails(id: String) = getRecipeDetailsUseCase.invoke(id)
        .onEach { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    _uiState.update { UiState(isLoading = true) }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        UiState(
                            error = UiText.RemoteString(result.message.toString())
                        )
                    }
                }

                is NetworkResult.Success -> {
                    _uiState.update {
                        UiState(data = result.data)
                    }
                }
            }
        }.launchIn(viewModelScope)
}

object RecipeDetails {

    data class UiState(
        val isLoading: Boolean = false,
        val error: UiText = UiText.Idle,
        val data: RecipeDetails? = null
    )

    sealed interface Navigation {

    }

    sealed interface Event {

        data class FetchRecipeDetails(val id: String) : Event
    }
}