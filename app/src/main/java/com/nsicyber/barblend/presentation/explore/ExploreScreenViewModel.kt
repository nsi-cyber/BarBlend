package com.nsicyber.barblend.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.domain.useCase.network.GetLatestCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.network.GetPopularCocktailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel
    @Inject
    constructor(
        private val getPopularCocktailsUseCase: GetPopularCocktailsUseCase,
        private val getLatestCocktailsUseCase: GetLatestCocktailsUseCase,
    ) : ViewModel() {
        private val _exploreScreenState = MutableStateFlow(ExploreScreenState(isLoading = true))
        val exploreScreenState = _exploreScreenState.asStateFlow()

        fun onEvent(event: ExploreScreenEvent) {
            when (event) {
                ExploreScreenEvent.RefreshPage -> {
                    setStateEmpty()
                    fetchCocktails()
                }

                ExploreScreenEvent.StartPage -> {
                    fetchCocktails()
                }
            }
        }

        private fun fetchCocktails() {
            viewModelScope.launch {
                combine(
                    getPopularCocktailsUseCase(),
                    getLatestCocktailsUseCase(),
                ) { popularResult, latestResult ->
                    Pair(popularResult, latestResult)
                }.onStart {
                    updateUiState { copy(isLoading = true) }
                }.onEach { (popularResult, latestResult) ->
                    updateUiState {
                        var newState = this

                        when (popularResult) {
                            is ApiResult.Error ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(popularCocktails = listOf()),
                                    )

                            is ApiResult.Success ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(popularCocktails = popularResult.data),
                                    )
                        }

                        when (latestResult) {
                            is ApiResult.Error ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(latestCocktails = listOf()),
                                    )

                            is ApiResult.Success ->
                                newState =
                                    newState.copy(
                                        data = newState.data.copy(latestCocktails = latestResult.data),
                                    )
                        }

                        if (popularResult is ApiResult.Success && latestResult is ApiResult.Success) {
                            newState = newState.copy(isLoading = false)
                        }

                        newState
                    }
                }.launchIn(this)
            }
        }

        private fun setStateEmpty() {
            updateUiState { ExploreScreenState() }
        }

        private fun updateUiState(block: ExploreScreenState.() -> ExploreScreenState) {
            _exploreScreenState.update(block)
        }
    }
