package com.nsicyber.barblend.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.barblend.common.ApiResult
import com.nsicyber.barblend.data.toModel
import com.nsicyber.barblend.domain.useCase.database.GetRecentCocktailsUseCase
import com.nsicyber.barblend.domain.useCase.network.SearchCocktailByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchCocktailByNameUseCase: SearchCocktailByNameUseCase,
    private val getRecentCocktailsUseCase: GetRecentCocktailsUseCase,
) : ViewModel() {


    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    private fun searchWithDebounce(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            searchCocktails(query)
        }
    }


    private fun setStateEmpty() {
        searchJob?.cancel()
        _searchScreenState.update { state ->
            state.copy(
                isSearchLoading = false,
                isPageLoading = false,
                data = state.data.copy(searchCocktails = null),
            )
        }
    }

    private fun searchCocktails(name: String) {
        viewModelScope.launch {


            searchCocktailByNameUseCase(name).onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _searchScreenState.update { state ->
                            state.copy(
                                isSearchLoading = false,
                                isPageLoading = false,
                                data = state.data.copy(searchCocktails = listOf()),
                            )
                        }

                    }

                    is ApiResult.Loading -> {
                        _searchScreenState.update { state ->
                            state.copy(
                                isSearchLoading = true,
                                isPageLoading = false,
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        if (result.data?.drinks.isNullOrEmpty()) {
                            _searchScreenState.update { state ->
                                state.copy(
                                    isSearchLoading = false,
                                    isPageLoading = false,
                                    data = state.data.copy(
                                        searchCocktails = listOf()
                                    )
                                )
                            }
                        } else {
                            _searchScreenState.update { state ->
                                state.copy(isSearchLoading = false,
                                    isPageLoading = false,
                                    data = state.data.copy(
                                        searchCocktails = result.data?.drinks?.map { it?.toModel() }
                                    )
                                )
                            }
                        }


                    }
                }
            }.launchIn(this)

        }
    }

    fun onEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.Search -> searchWithDebounce(event.text)
            is SearchScreenEvent.SetEmpty -> setStateEmpty()
        }
    }

    fun getRecent() {
        viewModelScope.launch {


            getRecentCocktailsUseCase().onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _searchScreenState.update { state ->
                            state.copy(
                                isSearchLoading = false,
                                isPageLoading = false,
                                data = state.data.copy(recentCocktails = listOf()),
                            )
                        }

                    }

                    is ApiResult.Loading -> {
                        _searchScreenState.update { state ->
                            state.copy(
                                isSearchLoading = false,
                                isPageLoading = true,
                            )
                        }
                    }

                    is ApiResult.Success -> {
                        _searchScreenState.update { state ->
                            state.copy(isSearchLoading = false,
                                isPageLoading = false,
                                data = state.data.copy(
                                    recentCocktails = result.data?.map { it.toModel() }
                                        ?.asReversed()
                                )
                            )
                        }

                    }
                }
            }.launchIn(this)

        }
    }

}