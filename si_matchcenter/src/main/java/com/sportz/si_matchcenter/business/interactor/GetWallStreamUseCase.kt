package com.sportz.si_matchcenter.business.interactor

import com.sportz.base.helper.Result
import com.sportz.si_matchcenter.business.domain.model.CustomMetaData
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetWallStreamUseCase @Inject constructor(
    private val repository: MatchCenterRepository
) {
    suspend operator fun invoke(matchId: String, eventState: EventState, inning: Int = 1): Result<CustomMetaData?> {
        return repository.getWallStream(matchId, eventState, inning)
    }
}
