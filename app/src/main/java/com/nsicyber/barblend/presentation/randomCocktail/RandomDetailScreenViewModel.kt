package com.nsicyber.barblend.presentation.randomCocktail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.toFavLocal
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.database.IsFavoriteCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToFavoriteUseCase
import com.nsicyber.barblend.domain.useCase.network.GetRandomCocktailUseCase
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
class RandomDetailScreenViewModel @Inject constructor(
    private val getRandomCocktailUseCase: GetRandomCocktailUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val isFavoriteCocktailUseCase: IsFavoriteCocktailUseCase,
    private val removeCocktailUseCase: RemoveCocktailUseCase,
) : ViewModel() {


    private val _randomDetailScreenState = MutableStateFlow(RandomDetailScreenState())
    val randomDetailScreenState = _randomDetailScreenState.asStateFlow()


    fun onEvent(event: RandomDetailScreenEvent) {
        when (event) {
            RandomDetailScreenEvent.AddToFavorites -> saveCocktailToFavorite()
            RandomDetailScreenEvent.OpenSuggestionBottomSheet -> {


                _randomDetailScreenState.update { state ->


                    state.copy(
                        bottomSheetData = state.bottomSheetData.copy(
                            text = "Save cocktail to Favorite List",
                            bottomSheetState = RandomBottomSheetState.onInput
                        )
                    )
                }
            }


            RandomDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()
            is RandomDetailScreenEvent.TypeSuggestion -> _randomDetailScreenState.update { state ->
                state.copy(
                    bottomSheetData = state.bottomSheetData.copy(
                        suggestion = event.str
                    )
                )
            }

            RandomDetailScreenEvent.ResetBottomSheet -> _randomDetailScreenState.update { state ->


                state.copy(
                    bottomSheetData = state.bottomSheetData.copy(
                        bottomSheetState = RandomBottomSheetState.onDismiss,
                        text = null
                    )
                )
            }

            RandomDetailScreenEvent.StartScreen -> getCocktailDetail()
        }
    }


    private fun getCocktailDetail() {
        viewModelScope.launch {
            getRandomCocktailUseCase().onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _randomDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = listOf()),
                            )
                        }
                        if (_randomDetailScreenState.value.data.isContent()) {
                            _randomDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _randomDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = result.data?.drinks?.map { it?.toModel() }),
                            )

                        }
                        if (_randomDetailScreenState.value.data.isContent()) {
                            _randomDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }


                        isCocktailFavorite(_randomDetailScreenState.value.data.cocktailDetail?.first()?.id)
                    }
                }
            }.launchIn(this)

        }
    }

    private fun isCocktailFavorite(id: String?) {
        viewModelScope.launch {
            isFavoriteCocktailUseCase(id).onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _randomDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(isFavorite = false),
                            )
                        }
                        if (_randomDetailScreenState.value.data.isContent()) {
                            _randomDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _randomDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(isFavorite = result.data),
                            )

                        }
                        if (_randomDetailScreenState.value.data.isContent()) {
                            _randomDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }
                }
            }.launchIn(this)

        }
    }

    private fun saveCocktailToFavorite() {
        viewModelScope.launch {
            _randomDetailScreenState.value.data.cocktailDetail?.first()?.toFavLocal()
                ?.let { model ->
                    saveToFavoriteUseCase(model.copy(suggestion = _randomDetailScreenState.value.bottomSheetData.suggestion.orEmpty())).onEach { result ->
                        when (result) {
                            is ApiResult.Error -> {
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Error While Favoriting",
                                            bottomSheetState = RandomBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = RandomBottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }

                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        data = state.data.copy(isFavorite = true),
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Cocktail Added to Favorites",
                                            bottomSheetState = RandomBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = RandomBottomSheetState.onDismiss
                                        )
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
            _randomDetailScreenState.value.data.cocktailDetail?.first()?.toFavLocal()
                ?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        when (result) {
                            is ApiResult.Error -> {
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Error While Removing",
                                            bottomSheetState = RandomBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = RandomBottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }

                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        data = state.data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Cocktail Removed from Favorites",
                                            bottomSheetState = RandomBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _randomDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = RandomBottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }
                        }
                    }.launchIn(this)
                }


        }
    }

}