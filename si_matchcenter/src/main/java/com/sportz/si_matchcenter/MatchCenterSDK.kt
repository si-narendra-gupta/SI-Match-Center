package com.sportz.si_matchcenter

import com.google.gson.Gson
import com.sportz.base.data.network.HttpClientFactory
import com.sportz.base.helper.KtorServiceHelper
import com.sportz.si_matchcenter.business.interactor.GetManhattanDataUseCase
import com.sportz.si_matchcenter.business.interactor.GetMatchListingUseCase
import com.sportz.si_matchcenter.business.interactor.GetSpiderGraphUseCase
import com.sportz.si_matchcenter.business.interactor.GetWallStreamUseCase
import com.sportz.si_matchcenter.data.mapper.CommentaryMapper
import com.sportz.si_matchcenter.data.mapper.MatchCenterMapper
import com.sportz.si_matchcenter.data.mapper.ParticipantMapper
import com.sportz.si_matchcenter.data.mapper.PlayerMapper
import com.sportz.si_matchcenter.data.mapper.SpiderGraphMapper
import com.sportz.si_matchcenter.data.remote.MatchCenterConfig
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.si_matchcenter.data.repository.MatchCenterRepositoryImpl
import com.sportz.si_matchcenter.data.service.MatchCenterService
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModelFactory

object MatchCenterSDK {
    private var config: SiFeedFixtureConfigContract? = null
    private val gson by lazy { Gson() }

    fun init(config: SiFeedFixtureConfigContract) {
        this.config = config
    }

    private fun getConfig(): SiFeedFixtureConfigContract {
        return config ?: throw IllegalStateException("MatchCenterSDK must be initialized with SiFeedFixtureConfigContract before use.")
    }

    fun provideViewModelFactory(): MatchCenterViewModelFactory {
        val fixtureConfig = getConfig()

        // Base dependencies from :base module (manually instantiated)
        val baseConfig = MatchCenterConfig(feedFixtureConfigContract = fixtureConfig)
        val httpClientFactory = HttpClientFactory(baseConfigContract = baseConfig)
        val ktorServiceHelper = KtorServiceHelper(httpClient = httpClientFactory)
        val matchCenterService = MatchCenterService(ktorServiceHelper = ktorServiceHelper)

        // Mappers
        val playerMapper = PlayerMapper()
        val participantMapper = ParticipantMapper(fixtureConfig, playerMapper)
        val matchCenterMapper = MatchCenterMapper(playerMapper, participantMapper)
        val spiderGraphMapper = SpiderGraphMapper()
        val commentaryMapper = CommentaryMapper()

        // Repository
        val repository = MatchCenterRepositoryImpl(
            matchCenterService = matchCenterService,
            matchCenterMapper = matchCenterMapper,
            spiderGraphMapper = spiderGraphMapper,
            commentaryMapper = commentaryMapper,
            siFeedFixtureConfigContract = fixtureConfig
        )

        // Use Cases
        val getMatchListingUseCase = GetMatchListingUseCase(repository)
        val getManhattanDataUseCase = GetManhattanDataUseCase(repository)
        val getSpiderGraphUseCase = GetSpiderGraphUseCase(repository)
        val getWallStreamUseCase = GetWallStreamUseCase(repository)

        return MatchCenterViewModelFactory(
            fixtureConfigContract = fixtureConfig,
            getMatchListingUseCase = getMatchListingUseCase,
            getManhattanDataUseCase = getManhattanDataUseCase,
            getSpiderGraphUseCase = getSpiderGraphUseCase,
            getWallStreamUseCase = getWallStreamUseCase,
            gson = gson
        )
    }
}
