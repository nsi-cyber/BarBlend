package com.nsicyber.barblend.presentation.explore


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.network.GetPopularCocktailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel @Inject constructor(
    private val getPopularCocktailsUseCase: GetPopularCocktailsUseCase,
    private val getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
) : ViewModel() {


    private val _exploreScreenState = MutableStateFlow(ExploreScreenState())
    val exploreScreenState = _exploreScreenState.asStateFlow()

    private fun getPopularCocktails() {
        viewModelScope.launch {
            getPopularCocktailsUseCase().onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _exploreScreenState.update { state ->
                            state.copy(data = state.data.copy(popularCocktails = listOf()))
                        }
                        if (_exploreScreenState.value.data.isContent()) {
                            _exploreScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _exploreScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(popularCocktails =
                                result.data?.drinks?.map { it?.toModel() })
                            )
                        }
                        if (_exploreScreenState.value.data.isContent()) {
                            _exploreScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }
                }
            }.launchIn(this)

        }
    }


    private fun setStateEmpty() {
        _exploreScreenState.update {
            ExploreScreenState()
        }
    }

    fun onEvent(event: ExploreScreenEvent) {
        when (event) {
            ExploreScreenEvent.RefreshPage -> {
                setStateEmpty()
                getPopularCocktails()
                getLatestCocktails()
            }

            ExploreScreenEvent.StartPage -> {
                getPopularCocktails()
                getLatestCocktails()
            }
        }
    }

    private fun getLatestCocktails() {
        viewModelScope.launch {
            getLatestCocktailsUseCase().onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _exploreScreenState.update { state ->
                            state.copy(data = state.data.copy(latestCocktails = listOf()))
                        }
                        if (_exploreScreenState.value.data.isContent()) {
                            _exploreScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }

                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        _exploreScreenState.update { state ->
                            state.copy(
                                data = state.data.copy(latestCocktails =
                                result.data?.drinks?.map { it?.toModel() })
                            )
                        }
                        if (_exploreScreenState.value.data.isContent()) {
                            _exploreScreenState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }
                }
            }.launchIn(this)

        }
    }


}