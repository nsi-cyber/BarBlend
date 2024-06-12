package com.nsicyber.barblend.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.domain.useCase.database.IsFavoriteCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToFavoriteUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToRecentUseCase
import com.nsicyber.barblend.domain.useCase.network.GetCocktailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailScreenViewModel
    @Inject
    constructor(
        private val getCocktailByIdUseCase: GetCocktailByIdUseCase,
        private val saveToRecentUseCase: SaveToRecentUseCase,
        private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
        private val isFavoriteCocktailUseCase: IsFavoriteCocktailUseCase,
        private val removeCocktailUseCase: RemoveCocktailUseCase,
    ) : ViewModel() {
        private val _cocktailDetailScreenState =
            MutableStateFlow(CocktailDetailScreenState(isLoading = true))
        val cocktailDetailScreenState = _cocktailDetailScreenState.asStateFlow()

        fun onEvent(event: CocktailDetailScreenEvent) {
            when (event) {
                CocktailDetailScreenEvent.AddToFavorites -> saveCocktailToFavorite()
                CocktailDetailScreenEvent.OpenSuggestionBottomSheet -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    bottomSheetState = BottomSheetState.OnInput,
                                ),
                        )
                    }
                }

                CocktailDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()
                is CocktailDetailScreenEvent.TypeSuggestion -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    suggestion = event.str,
                                ),
                        )
                    }
                }

                CocktailDetailScreenEvent.ResetBottomSheet -> {
                    updateUiState {
                        copy(
                            bottomSheetData =
                                bottomSheetData.copy(
                                    bottomSheetState = BottomSheetState.OnDismiss,
                                    suggestion = null,
                                ),
                        )
                    }
                }

                is CocktailDetailScreenEvent.StartPage -> {
                    getCocktailDetailAndFavoriteStatus(event.id)
                }
            }
        }

        private fun getCocktailDetailAndFavoriteStatus(id: String?) {
            viewModelScope.launch {
                combine(
                    getCocktailByIdUseCase(id),
                    isFavoriteCocktailUseCase(id),
                ) { cocktailResult, favoriteResult ->
                    Pair(cocktailResult, favoriteResult)
                }.onStart {
                    updateUiState {
                        copy(isLoading = true)
                    }
                }.onEach { (cocktailResult, favoriteResult) ->
                    updateUiState {
                        var newState = this
                        when (cocktailResult) {
                            is ApiResult.Error ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(cocktailDetail = null),
                                    )

                            is ApiResult.Success ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(cocktailDetail = cocktailResult.data),
                                    )
                        }
                        when (favoriteResult) {
                            is DaoResult.Error ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(isFavorite = false),
                                    )

                            is DaoResult.Success ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(isFavorite = favoriteResult.data),
                                    )
                        }
                        if (cocktailResult is ApiResult.Success && favoriteResult is DaoResult.Success) {
                            newState = newState.copy(isLoading = false)
                            saveCocktailToRecent()
                        }
                        newState
                    }
                }.launchIn(this)
            }
        }

        private fun saveCocktailToRecent() {
            viewModelScope.launch {
                _cocktailDetailScreenState.value.data.cocktailDetail?.let { model ->
                    saveToRecentUseCase(
                        model.copy(suggestion = _cocktailDetailScreenState.value.bottomSheetData.suggestion.orEmpty()),
                    ).collect()
                }
            }
        }

        private fun saveCocktailToFavorite() {
            viewModelScope.launch {
                _cocktailDetailScreenState.value.data.cocktailDetail?.let { model ->
                    saveToFavoriteUseCase(
                        model.copy(suggestion = _cocktailDetailScreenState.value.bottomSheetData.suggestion.orEmpty()),
                    ).onEach { result ->
                        when (result) {
                            is DaoResult.Error -> {
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnError,
                                            ),
                                    )
                                }
                                delay(1000)
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnDismiss,
                                            ),
                                    )
                                }
                            }

                            is DaoResult.Success -> {
                                updateUiState {
                                    copy(
                                        data = data.copy(isFavorite = true),
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnFavoriteMessage,
                                            ),
                                    )
                                }
                                delay(1000)
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnDismiss,
                                            ),
                                    )
                                }
                            }
                        }
                    }.launchIn(this)
                }
            }
        }

        private fun removeCocktailFromFavorite() {
            viewModelScope.launch {
                _cocktailDetailScreenState.value.data.cocktailDetail?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        when (result) {
                            is DaoResult.Error -> {
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnError,
                                            ),
                                    )
                                }
                                delay(1000)
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnDismiss,
                                            ),
                                    )
                                }
                            }

                            is DaoResult.Success -> {
                                updateUiState {
                                    copy(
                                        data = data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnRemoveMessage,
                                            ),
                                    )
                                }
                                delay(1000)
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                bottomSheetState = BottomSheetState.OnDismiss,
                                            ),
                                    )
                                }
                            }
                        }
                    }.launchIn(this)
                }
            }
        }

        private fun updateUiState(block: CocktailDetailScreenState.() -> CocktailDetailScreenState) {
            _cocktailDetailScreenState.update(block)
        }
    }
