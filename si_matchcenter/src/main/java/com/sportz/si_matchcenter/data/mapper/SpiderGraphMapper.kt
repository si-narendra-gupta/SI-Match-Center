package com.sportz.si_matchcenter.data.mapper

import com.sportz.base.helper.EntityMapper
import com.sportz.si_matchcenter.business.domain.model.SpiderRegion
import com.sportz.si_matchcenter.business.domain.model.SpiderShot
import com.sportz.si_matchcenter.business.domain.model.SpiderZone
import com.sportz.si_matchcenter.data.model.spidergraph.SpiderGraph
import javax.inject.Inject

class SpiderGraphMapper @Inject constructor() : EntityMapper<SpiderGraph, SpiderZone> {

    override fun toDomain(entity: SpiderGraph): SpiderZone {
        val allShots = mutableListOf<SpiderShot>()

        // Explicit mapping of zone index (1-8) to region names
        val regionNames = listOf(
            "Square Leg", // Zone 1
            "Fine Leg",   // Zone 2
            "Third Man",  // Zone 3
            "Point",      // Zone 4
            "Cover",      // Zone 5
            "Mid Off",    // Zone 6
            "Mid On",     // Zone 7
            "Mid Wicket", // Zone 8
        )

        // Initialize with 0 runs to ensure all regions exist and preserve order
        val regionMap = regionNames.associateWith { 0 }.toMutableMap()

        entity.batsmen?.forEach { (_, batsmanDetails) ->
            batsmanDetails.shots?.forEach { shot ->
                val runs = shot.runs?.toIntOrNull() ?: 0
                val distance = shot.distance?.toIntOrNull() ?: 0
                val angle = shot.angle?.toFloatOrNull() ?: 0f
                val zoneIndex = shot.zone?.toIntOrNull() ?: 0

                allShots.add(SpiderShot(angle = angle, runs = runs, distance = distance))

                // Map zone 1-8 to the region list (0-7 index)
                if (zoneIndex in 1..8) {
                    val regionName = regionNames[zoneIndex - 1]
                    regionMap[regionName] = (regionMap[regionName] ?: 0) + runs
                }
            }
        }

        // Convert map back to list of SpiderRegion, maintaining defined order
        val regionsList = regionNames.map { name ->
            SpiderRegion(name, regionMap[name] ?: 0)
        }

        return SpiderZone(
            shotsList = allShots,
            regionsList = regionsList
        )
    }
}
