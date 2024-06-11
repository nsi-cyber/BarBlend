package com.nsicyber.barblend.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.common.DaoResult
import com.nsicyber.barblend.domain.useCase.database.GetRecentCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.network.SearchCocktailByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        private val searchCocktailByNameUseCase: SearchCocktailByNameUseCase,
        private val getRecentCocktailsUseCase: GetRecentCocktailsUseCase,
    ) : ViewModel() {
        private val _searchScreenState = MutableStateFlow(SearchScreenState(isPageLoading = true))
        val searchScreenState = _searchScreenState.asStateFlow()

        private var searchJob: Job? = null

        fun onEvent(event: SearchScreenEvent) {
            when (event) {
                is SearchScreenEvent.Search -> searchWithDebounce(event.text)
                is SearchScreenEvent.SetEmpty -> setStateEmpty()
                SearchScreenEvent.StartPage -> getRecent()
            }
        }

        private fun searchWithDebounce(query: String) {
            searchJob?.cancel()
            searchJob =
                viewModelScope.launch {
                    delay(400)
                    searchCocktails(query)
                }
        }

        private fun setStateEmpty() {
            searchJob?.cancel()
            updateUiState {
                copy(
                    isSearchLoading = false,
                    isPageLoading = false,
                    data = data.copy(searchCocktails = null),
                )
            }
        }

        private fun searchCocktails(name: String) {
            viewModelScope.launch {
                searchCocktailByNameUseCase(name).onStart {
                    updateUiState {
                        copy(
                            isSearchLoading = true,
                            isPageLoading = false,
                        )
                    }
                }.onEach { result ->
                    updateUiState {
                        when (result) {
                            is ApiResult.Error ->
                                copy(
                                    isSearchLoading = false,
                                    isPageLoading = false,
                                    data = data.copy(searchCocktails = listOf()),
                                )

                            is ApiResult.Success -> {
                                if (result.data?.isEmpty() == true) {
                                    copy(
                                        isSearchLoading = false,
                                        isPageLoading = false,
                                        data = data.copy(searchCocktails = listOf()),
                                    )
                                } else {
                                    copy(
                                        isSearchLoading = false,
                                        isPageLoading = false,
                                        data = data.copy(searchCocktails = result.data),
                                    )
                                }
                            }
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun getRecent() {
            viewModelScope.launch {
                getRecentCocktailsUseCase().onStart {
                    updateUiState {
                        copy(
                            isSearchLoading = false,
                            isPageLoading = true,
                        )
                    }
                }.onEach { result ->
                    updateUiState {
                        when (result) {
                            is DaoResult.Error ->
                                copy(
                                    isSearchLoading = false,
                                    isPageLoading = false,
                                    data = data.copy(recentCocktails = listOf()),
                                )

                            is DaoResult.Success ->
                                copy(
                                    isSearchLoading = false,
                                    isPageLoading = false,
                                    data = data.copy(recentCocktails = result.data?.asReversed()),
                                )
                        }
                    }
                }.launchIn(this)
            }
        }

        private fun updateUiState(block: SearchScreenState.() -> SearchScreenState) {
            _searchScreenState.update(block)
        }
    }
