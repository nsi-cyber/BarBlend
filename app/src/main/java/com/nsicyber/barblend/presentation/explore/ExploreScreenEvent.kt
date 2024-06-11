package com.nsicyber.barblend.presentation.explore

sealed class ExploreScreenEvent {
    data object RefreshPage : ExploreScreenEvent()

    data object StartPage : ExploreScreenEvent()
}
