package com.sportz.si_matchcenter.data.mapper

import com.sportz.si_matchcenter.business.domain.model.BallDetail
import com.sportz.si_matchcenter.business.domain.model.CustomMetaData
import com.sportz.si_matchcenter.business.domain.model.DomainOverSummary
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.data.model.lastsixball.CommentaryDataModel
import javax.inject.Inject

class CommentaryMapper @Inject constructor() {

    fun toDomain(response: CommentaryDataModel, eventState: EventState): CustomMetaData {
        val commentaryList = response.assets?.mapNotNull { asset ->
            asset?.customMetadata?.getAssetDetail()?.let { detail ->
                BallDetail(
                    overLabel = detail.over,
                    overNo = detail.overNo,
                    ballNo = detail.ballNo,
                    runs = detail.runs,
                    commentary = detail.commentary,
                    isBall = detail.isBall,
                    isWicket = detail.isWicket,
                    bowlerName = detail.bowlerName,
                    batsmanName = detail.batsmanName,
                    detail = detail.detail,
                    overSummary = detail.overSummary?.let { summary ->
                        DomainOverSummary(
                            over = summary.over,
                            runs = summary.runs,
                            wickets = summary.wickets,
                            score = summary.score
                        )
                    }
                )
            }
        } ?: emptyList()

        return if (eventState == EventState.UPCOMING) {
            val ballAssetDetail = response.assets?.firstNotNullOfOrNull { asset ->
                asset?.customMetadata?.getAssetDetail()
            }
            CustomMetaData(
                commentary = ballAssetDetail?.commentary,
                ballByBall = null,
                commentaryList = commentaryList
            )
        } else {
            val ballAssetDetail = response.assets?.firstNotNullOfOrNull { asset ->
                asset?.customMetadata?.getAssetDetail()?.takeIf { it.isBall == true }
            }
            CustomMetaData(
                commentary = ballAssetDetail?.commentary,
                ballByBall = ballAssetDetail?.thisOver?.split(",")
                    ?.filter { it.isNotBlank() } ?: emptyList(),
                commentaryList = commentaryList
            )
        }
    }
}
