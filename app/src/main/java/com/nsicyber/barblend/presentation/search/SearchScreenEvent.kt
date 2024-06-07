package com.nsicyber.barblend.presentation.search

sealed class SearchScreenEvent {
    data class Search(val text: String) : SearchScreenEvent()
    data object SetEmpty : SearchScreenEvent()
}