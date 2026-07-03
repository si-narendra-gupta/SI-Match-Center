package com.sportz.si_matchcenter.di

import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
import com.sportz.si_matchcenter.data.repository.MatchCenterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface MatchCenterModule {

    @Binds
    @ViewModelScoped
    fun bindMatchCenterRepository(matchCenterRepositoryImpl: MatchCenterRepositoryImpl): MatchCenterRepository
}
