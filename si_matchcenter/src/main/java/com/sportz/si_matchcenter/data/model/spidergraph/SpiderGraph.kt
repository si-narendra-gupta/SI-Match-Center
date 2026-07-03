package com.sportz.si_matchcenter.data.model.spidergraph


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpiderGraph(
    @SerialName("Batsmen")
    val batsmen: HashMap<String, Batsmen>?
)
