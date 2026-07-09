package com.sportz.si_matchcenter.business.interactor

import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
class GetManhattanDataUseCase(
    private val repository: MatchCenterRepository
) {
    suspend operator fun invoke(gameID: String, inning: Int) = repository.getManhattanData(gameID, inning)
}
