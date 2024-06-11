package com.nsicyber.barblend.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.toFavLocal
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.database.IsFavoriteCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToFavoriteUseCase
import com.nsicyber.barblend.domain.useCase.network.GetCocktailByIdUseCase
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
class CocktailDetailScreenViewModel @Inject constructor(
    private val getCocktailByIdUseCase: GetCocktailByIdUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val isFavoriteCocktailUseCase: IsFavoriteCocktailUseCase,
    private val removeCocktailUseCase: RemoveCocktailUseCase,
) : ViewModel() {


    private val _cocktailDetailScreenState = MutableStateFlow(CocktailDetailScreenState())
    val cocktailDetailScreenState = _cocktailDetailScreenState.asStateFlow()


    fun onEvent(event: CocktailDetailScreenEvent) {
        when (event) {
            CocktailDetailScreenEvent.AddToFavorites -> saveCocktailToFavorite()
            CocktailDetailScreenEvent.OpenSuggestionBottomSheet -> {


                _cocktailDetailScreenState.update { state ->


                    state.copy(
                        bottomSheetData = state.bottomSheetData.copy(
                            text = "Save cocktail to Favorite List",
                            bottomSheetState = BottomSheetState.onInput
                        )
                    )
                }
            }


            CocktailDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()
            is CocktailDetailScreenEvent.TypeSuggestion -> _cocktailDetailScreenState.update { state ->
                state.copy(
                    bottomSheetData = state.bottomSheetData.copy(
                        suggestion = event.str
                    )
                )
            }

            CocktailDetailScreenEvent.ResetBottomSheet -> _cocktailDetailScreenState.update { state ->


                state.copy(
                    bottomSheetData = state.bottomSheetData.copy(
                        bottomSheetState = BottomSheetState.onDismiss,
                        text = null,
                        suggestion = null

                    )
                )
            }



            is CocktailDetailScreenEvent.StartPage -> {
                getCocktailDetail(event.id)
                isCocktailFavorite(event.id)
            }
        }
    }


    private fun getCocktailDetail(id: String?) {
        viewModelScope.launch {
            getCocktailByIdUseCase(id).onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _cocktailDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = listOf()),
                            )
                        }
                        if (_cocktailDetailScreenState.value.data.isContent()) {
                            _cocktailDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _cocktailDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = result.data?.drinks?.map { it?.toModel() }),
                            )

                        }
                        if (_cocktailDetailScreenState.value.data.isContent()) {
                            _cocktailDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
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
                        _cocktailDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(isFavorite = false),
                            )
                        }
                        if (_cocktailDetailScreenState.value.data.isContent()) {
                            _cocktailDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _cocktailDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(isFavorite = result.data),
                            )

                        }
                        if (_cocktailDetailScreenState.value.data.isContent()) {
                            _cocktailDetailScreenState.update { state ->
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
            _cocktailDetailScreenState.value.data.cocktailDetail?.first()?.toFavLocal()
                ?.let { model ->
                    saveToFavoriteUseCase(model.copy(suggestion = _cocktailDetailScreenState.value.bottomSheetData.suggestion.orEmpty())).onEach { result ->
                        when (result) {
                            is ApiResult.Error -> {
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Error While Favoriting",
                                            bottomSheetState = BottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = BottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }

                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        data = state.data.copy(isFavorite = true),
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Cocktail Added to Favorites",
                                            bottomSheetState = BottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = BottomSheetState.onDismiss
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
            _cocktailDetailScreenState.value.data.cocktailDetail?.first()?.toFavLocal()
                ?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        when (result) {
                            is ApiResult.Error -> {
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Error While Removing",
                                            bottomSheetState = BottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = BottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }

                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        data = state.data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Cocktail Removed from Favorites",
                                            bottomSheetState = BottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _cocktailDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = BottomSheetState.onDismiss
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