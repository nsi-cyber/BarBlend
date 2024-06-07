package com.nsicyber.barblend.presentation.favoriteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.toFavLocal
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.database.GetCocktailDetailsUseCase
import com.nsicyber.barblend.domain.useCase.database.IsFavoriteCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import com.nsicyber.barblend.domain.useCase.database.SaveToFavoriteUseCase
import com.nsicyber.barblend.domain.useCase.network.GetCocktailByIdUseCase
import com.nsicyber.barblend.presentation.detail.BottomSheetState
import com.nsicyber.barblend.presentation.detail.CocktailDetailScreenEvent
import com.nsicyber.barblend.presentation.detail.CocktailDetailScreenState
import com.nsicyber.barblend.presentation.detail.isContent
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
class FavoriteDetailScreenViewModel @Inject constructor(
    private val getCocktailDetailsUseCase: GetCocktailDetailsUseCase,
    private val removeCocktailUseCase: RemoveCocktailUseCase,
) : ViewModel() {


    private val _favoriteDetailScreenState = MutableStateFlow(FavoriteDetailScreenState())
    val favoriteDetailScreenState = _favoriteDetailScreenState.asStateFlow()


    fun onEvent(event: FavoriteDetailScreenEvent) {
        when (event) {


            FavoriteDetailScreenEvent.RemoveFromFavorites -> removeCocktailFromFavorite()

            FavoriteDetailScreenEvent.ResetBottomSheet -> _favoriteDetailScreenState.update { state ->


                state.copy(
                    bottomSheetData = state.bottomSheetData.copy(
                        bottomSheetState = FavoriteDetailBottomSheetState.onDismiss,
                        text = null
                    )
                )
            }
        }
    }


    fun getCocktailDetail(id: String?) {
        viewModelScope.launch {
            getCocktailDetailsUseCase(id).onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _favoriteDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = listOf()),
                            )
                        }
                        if (_favoriteDetailScreenState.value.data.isContent()) {
                            _favoriteDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _favoriteDetailScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(cocktailDetail = listOf(result.data?.toModel())  ),
                            )

                        }
                        if (_favoriteDetailScreenState.value.data.isContent()) {
                            _favoriteDetailScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }
                }
            }.launchIn(this)

        }
    }



    private fun removeCocktailFromFavorite() {
        viewModelScope.launch {
            _favoriteDetailScreenState.value.data.cocktailDetail?.first()?.toFavLocal()
                ?.let { model ->
                    removeCocktailUseCase(model).onEach { result ->
                        when (result) {
                            is ApiResult.Error -> {
                                _favoriteDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Error While Removing",
                                            bottomSheetState = FavoriteDetailBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _favoriteDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = FavoriteDetailBottomSheetState.onDismiss
                                        )
                                    )
                                }
                            }

                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                _favoriteDetailScreenState.update { state ->
                                    state.copy(isRemoved = true,
                                        data = state.data.copy(isFavorite = false),
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            text = "Cocktail Removed from Favorites",
                                            bottomSheetState = FavoriteDetailBottomSheetState.onMessage
                                        )
                                    )
                                }
                                delay(1000)
                                _favoriteDetailScreenState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        bottomSheetData = state.bottomSheetData.copy(
                                            bottomSheetState = FavoriteDetailBottomSheetState.onDismiss
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