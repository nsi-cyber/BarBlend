package com.nsicyber.barblend.presentation.randomCocktail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.domain.useCase.database.IsFavoriteCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToFavoriteUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToRecentUseCase
import com.nsicyber.barblend.domain.useCase.network.GetRandomCocktailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomDetailScreenViewModel
    @Inject
    constructor(
        private val getRandomCocktailUseCase: GetRandomCocktailUseCase,
        private val saveToRecentUseCase: SaveToRecentUseCase,
        private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
        private val isFavoriteCocktailUseCase: IsFavoriteCocktailUseCase,
        private val removeCocktailUseCase: RemoveCocktailUseCase,
    ) : ViewModel() {
        private val _randomDetailScreenState =
            MutableStateFlow(RandomDetailScreenState(isLoading = true))
        val randomDetailScreenState = _randomDetailScreenState.asStateFlow()

        fun onEvent(event: RandomDetailScreenEvent) {
            when (event) {
                RandomDetailScreenEvent.AddToFavorites -> saveCocktailToFavorite()
                RandomDetailScreenEvent.OpenSuggestionBottomSheet -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    text = "Save cocktail to Favorite List",
                                    bottomSheetState = RandomBottomSheetState.onInput,
                                ),
                        )
                    }
                }

                RandomDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()
                is RandomDetailScreenEvent.TypeSuggestion -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    suggestion = event.str,
                                ),
                        )
                    }
                }

                RandomDetailScreenEvent.ResetBottomSheet -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    bottomSheetState = RandomBottomSheetState.onDismiss,
                                    text = null,
                                    suggestion = null,
                                ),
                        )
                    }
                }

                RandomDetailScreenEvent.StartScreen -> getCocktailDetail()
            }
        }

        private fun getCocktailDetail() {
            viewModelScope.launch {
                getRandomCocktailUseCase().onEach { result ->
                    updateUiState {
                        when (result) {
                            is ApiResult.Error ->
                                copy(
                                    data = data.copy(cocktailDetail = null),
                                )

                            is ApiResult.Success -> {
                                copy(
                                    data = data.copy(cocktailDetail = result.data),
                                ).also {
                                    isCocktailFavorite(result.data?.id)
                                    saveCocktailToRecent()
                                }
                            }
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun saveCocktailToRecent() {
            viewModelScope.launch {
                _randomDetailScreenState.value.data.cocktailDetail?.let { model ->
                    saveToRecentUseCase(
                        model.copy(suggestion = _randomDetailScreenState.value.bottomSheetData.suggestion.orEmpty()),
                    ).collect()
                }
            }
        }

        private fun isCocktailFavorite(id: String?) {
            viewModelScope.launch {
                isFavoriteCocktailUseCase(id).onEach { result ->
                    updateUiState {
                        when (result) {
                            is DaoResult.Error ->
                                copy(
                                    data = data.copy(isFavorite = false),
                                )

                            is DaoResult.Success ->
                                copy(
                                    data = data.copy(isFavorite = result.data),
                                )
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun saveCocktailToFavorite() {
            viewModelScope.launch {
                _randomDetailScreenState.value.data.cocktailDetail?.let { model ->
                    saveToFavoriteUseCase(
                        model.copy(suggestion = _randomDetailScreenState.value.bottomSheetData.suggestion.orEmpty()),
                    ).onEach { result ->
                        updateUiState {
                            when (result) {
                                is DaoResult.Error -> {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Error While Favoriting",
                                                bottomSheetState = RandomBottomSheetState.onMessage,
                                            ),
                                    ).also {
                                        scheduleBottomSheetDismiss()
                                    }
                                }

                                is DaoResult.Success -> {
                                    copy(
                                        data = data.copy(isFavorite = true),
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Cocktail Added to Favorites",
                                                bottomSheetState = RandomBottomSheetState.onMessage,
                                            ),
                                    ).also {
                                        scheduleBottomSheetDismiss()
                                    }
                                }
                            }
                        }
                    }.launchIn(this)
                }
            }
        }

        private fun removeCocktailFromFavorite() {
            viewModelScope.launch {
                _randomDetailScreenState.value.data.cocktailDetail?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        updateUiState {
                            when (result) {
                                is DaoResult.Error -> {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Error While Removing",
                                                bottomSheetState = RandomBottomSheetState.onMessage,
                                            ),
                                    ).also {
                                        scheduleBottomSheetDismiss()
                                    }
                                }

                                is DaoResult.Success -> {
                                    copy(
                                        data = data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Cocktail Removed from Favorites",
                                                bottomSheetState = RandomBottomSheetState.onMessage,
                                            ),
                                    ).also {
                                        scheduleBottomSheetDismiss()
                                    }
                                }
                            }
                        }
                    }.launchIn(this)
                }
            }
        }

        private fun updateUiState(block: RandomDetailScreenState.() -> RandomDetailScreenState) {
            _randomDetailScreenState.update(block)
        }

        private fun scheduleBottomSheetDismiss() {
            viewModelScope.launch {
                delay(1000)
                updateUiState {
                    copy(
                        isLoading = false,
                        bottomSheetData =
                            bottomSheetData.copy(
                                bottomSheetState = RandomBottomSheetState.onDismiss,
                            ),
                    )
                }
            }
        }
    }
