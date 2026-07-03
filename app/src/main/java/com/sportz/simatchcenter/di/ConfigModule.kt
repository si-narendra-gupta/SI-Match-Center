package com.sportz.simatchcenter.di

import com.sportz.base.helper.BaseConfigContract
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.simatchcenter.data.remote.AppConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindBaseConfigContract(appConfig: AppConfig): BaseConfigContract

    @Binds
    @Singleton
    abstract fun provideSiFeedFixtureCenterConfig(appConfig: AppConfig): SiFeedFixtureConfigContract
}
