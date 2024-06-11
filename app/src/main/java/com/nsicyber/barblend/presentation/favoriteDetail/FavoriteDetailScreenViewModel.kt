package com.nsicyber.barblend.presentation.favoriteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.domain.useCase.database.GetCocktailDetailsUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailScreenViewModel
    @Inject
    constructor(
        private val getCocktailDetailsUseCase: GetCocktailDetailsUseCase,
        private val removeCocktailUseCase: RemoveCocktailUseCase,
    ) : ViewModel() {
        private val _favoriteDetailScreenState =
            MutableStateFlow(FavoriteDetailScreenState(isLoading = true))
        val favoriteDetailScreenState = _favoriteDetailScreenState.asStateFlow()

        fun onEvent(event: FavoriteDetailScreenEvent) {
            when (event) {
                FavoriteDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()
                FavoriteDetailScreenEvent.ResetBottomSheet -> resetBottomSheet()
                is FavoriteDetailScreenEvent.StartPage -> getCocktailDetail(event.id)
            }
        }

        private fun getCocktailDetail(id: String?) {
            viewModelScope.launch {
                getCocktailDetailsUseCase(id).onEach { result ->
                    updateUiState {
                        when (result) {
                            is DaoResult.Error ->
                                copy(
                                    data = data.copy(cocktailDetail = null),
                                )

                            is DaoResult.Success ->
                                copy(
                                    isLoading = false,
                                    data = data.copy(cocktailDetail = result.data),
                                )
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun removeCocktailFromFavorite() {
            viewModelScope.launch {
                _favoriteDetailScreenState.value.data.cocktailDetail?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        updateUiState {
                            when (result) {
                                is DaoResult.Error -> {
                                    copy(
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Error While Removing",
                                                bottomSheetState = FavoriteDetailBottomSheetState.onMessage,
                                            ),
                                    ).also {
                                        scheduleBottomSheetDismiss()
                                    }
                                }

                                is DaoResult.Success -> {
                                    copy(
                                        isRemoved = true,
                                        data = data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData =
                                            bottomSheetData.copy(
                                                text = "Cocktail Removed from Favorites",
                                                bottomSheetState = FavoriteDetailBottomSheetState.onMessage,
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

        private fun resetBottomSheet() {
            updateUiState {
                copy(
                    bottomSheetData =
                        bottomSheetData.copy(
                            bottomSheetState = FavoriteDetailBottomSheetState.onDismiss,
                            text = null,
                            suggestion = null,
                        ),
                )
            }
        }

        private fun updateUiState(block: FavoriteDetailScreenState.() -> FavoriteDetailScreenState) {
            _favoriteDetailScreenState.update(block)
        }

        private fun scheduleBottomSheetDismiss() {
            viewModelScope.launch {
                delay(1000)
                updateUiState {
                    copy(
                        bottomSheetData =
                            bottomSheetData.copy(
                                bottomSheetState = FavoriteDetailBottomSheetState.onDismiss,
                            ),
                    )
                }
            }
        }
    }
