package com.nsicyber.barblend.presentation.favoriteList


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.model.CocktailModel
import com.nsicyber.barblend.data.toFavLocal
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.database.GetFavoriteCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.database.RemoveCocktailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteListScreenViewModel @Inject constructor(
    private val getFavoriteCocktailsUseCase: GetFavoriteCocktailsUseCase,
    private val removeCocktailUseCase: RemoveCocktailUseCase,
) : ViewModel() {


    private val _favoriteListScreenState = MutableStateFlow(FavoriteListScreenState())
    val favoriteListScreenState = _favoriteListScreenState.asStateFlow()


    fun onEvent(event: FavoriteListScreenEvent) {
        when (event) {
            is FavoriteListScreenEvent.Remove -> removeFromFavorite(event.model)
        }
    }

    private fun removeFromFavorite(model: CocktailModel) {
        viewModelScope.launch {
            removeCocktailUseCase(model.toFavLocal()).onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        getFavorites()
                    }

                    is ApiResult.Loading -> {
                        _favoriteListScreenState.update { state ->
                            state.copy(
                                isLoading = true,
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        getFavorites()
                    }
                }
            }.launchIn(this)

        }
    }


    fun getFavorites() {
        viewModelScope.launch {
            getFavoriteCocktailsUseCase().onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _favoriteListScreenState.update { state ->
                            state.copy(
                                isLoading = false,
                                data = state.data.copy(favoriteCocktails = listOf()),
                            )
                        }

                    }

                    is ApiResult.Loading -> {
                        _favoriteListScreenState.update { state ->
                            state.copy(
                                isLoading = false,
                                )
                        }
                    }

                    is ApiResult.Success -> {
                        _favoriteListScreenState.update { state ->
                            state.copy(isLoading = false, data = state.data.copy(
                                favoriteCocktails = result.data?.map { it.toModel()}
                            ))
                        }

                    }
                }
            }.launchIn(this)

        }
    }

}