package com.sportz.si_matchcenter.business.domain.model

data class SpiderShot(val angle: Float, val runs: Int,val distance: Int)
data class SpiderRegion(val name: String, val runs: Int)

data class SpiderZone(
    val shotsList: List<SpiderShot>, val regionsList: List<SpiderRegion>
)