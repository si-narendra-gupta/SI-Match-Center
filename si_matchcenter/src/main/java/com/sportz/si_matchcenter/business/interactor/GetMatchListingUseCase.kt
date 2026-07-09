package com.sportz.si_matchcenter.business.interactor

import com.sportz.base.helper.Result
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
class GetMatchListingUseCase(
    private val repository: MatchCenterRepository
) {
    suspend operator fun invoke(gameID: String): Result<IPLMatch> {
        return repository.getMatchListing(gameID)
    }
}
