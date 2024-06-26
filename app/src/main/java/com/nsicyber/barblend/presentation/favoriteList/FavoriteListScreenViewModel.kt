package com.nsicyber.barblend.presentation.favoriteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.domain.useCase.database.GetFavoriteCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListScreenViewModel
    @Inject
    constructor(
        private val getFavoriteCocktailsUseCase: GetFavoriteCocktailsUseCase,
        private val removeCocktailUseCase: RemoveCocktailUseCase,
    ) : ViewModel() {
        private val _favoriteListScreenState =
            MutableStateFlow(FavoriteListScreenState(isLoading = true))
        val favoriteListScreenState = _favoriteListScreenState.asStateFlow()

        fun onEvent(event: FavoriteListScreenEvent) {
            when (event) {
                is FavoriteListScreenEvent.Remove -> removeFromFavorite(event.model)
                FavoriteListScreenEvent.StartPage -> getFavorites()
            }
        }

        private fun removeFromFavorite(model: CocktailModel) {
            viewModelScope.launch {
                removeCocktailUseCase(model).onStart {
                    updateUiState { copy(isLoading = true) }
                }.onEach { result ->
                    when (result) {
                        is DaoResult.Error, is DaoResult.Success -> getFavorites()
                    }
                }.launchIn(this)
            }
        }

        private fun getFavorites() {
            viewModelScope.launch {
                getFavoriteCocktailsUseCase().onStart {
                    updateUiState { copy(isLoading = true) }
                }.onEach { result ->
                    updateUiState {
                        when (result) {
                            is DaoResult.Error ->
                                copy(
                                    isLoading = false,
                                    data = data.copy(favoriteCocktails = listOf()),
                                )

                            is DaoResult.Success ->
                                copy(
                                    isLoading = false,
                                    data = data.copy(favoriteCocktails = result.data?.asReversed()),
                                )
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun updateUiState(block: FavoriteListScreenState.() -> FavoriteListScreenState) {
            _favoriteListScreenState.update(block)
        }
    }
