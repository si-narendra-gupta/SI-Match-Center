package com.sportz.si_matchcenter.business.interactor

import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
import javax.inject.Inject

class GetSpiderGraphUseCase @Inject constructor(
    private val repository: MatchCenterRepository
) {
    suspend operator fun invoke(gameID: String, inning: Int) = repository.getSpiderGraph(gameID, inning)
}
