package com.sportz.si_matchcenter.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sportz.si_matchcenter.business.interactor.GetManhattanDataUseCase
import com.sportz.si_matchcenter.business.interactor.GetMatchListingUseCase
import com.sportz.si_matchcenter.business.interactor.GetSpiderGraphUseCase
import com.sportz.si_matchcenter.business.interactor.GetWallStreamUseCase
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract

class MatchCenterViewModelFactory(
    private val fixtureConfigContract: SiFeedFixtureConfigContract,
    private val getMatchListingUseCase: GetMatchListingUseCase,
    private val getManhattanDataUseCase: GetManhattanDataUseCase,
    private val getSpiderGraphUseCase: GetSpiderGraphUseCase,
    private val getWallStreamUseCase: GetWallStreamUseCase,
    private val gson: Gson
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchCenterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchCenterViewModel(
                fixtureConfigContract,
                getMatchListingUseCase,
                getManhattanDataUseCase,
                getSpiderGraphUseCase,
                getWallStreamUseCase,
                gson
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
