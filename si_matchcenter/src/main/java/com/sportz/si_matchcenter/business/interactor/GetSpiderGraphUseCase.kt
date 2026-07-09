package com.sportz.si_matchcenter.business.interactor

import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
class GetSpiderGraphUseCase(
    private val repository: MatchCenterRepository
) {
    suspend operator fun invoke(gameID: String, inning: Int) = repository.getSpiderGraph(gameID, inning)
}
