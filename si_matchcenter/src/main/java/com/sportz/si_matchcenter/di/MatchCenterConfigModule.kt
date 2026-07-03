package com.sportz.si_matchcenter.di

import com.sportz.base.helper.BaseConfigContract
import com.sportz.si_matchcenter.data.remote.MatchCenterConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MatchCenterConfigModule {

    @Binds
    @Singleton
    abstract fun provideBaseConfigContract(matchCenterConfig: MatchCenterConfig): BaseConfigContract

}
