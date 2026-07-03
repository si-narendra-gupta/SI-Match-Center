package com.sportz.base.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sportz.base.data.network.HttpClientFactory
import com.sportz.base.helper.BaseConfigContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideKtorClient (
        baseInfo: BaseConfigContract,
    ): HttpClientFactory {
        return HttpClientFactory(baseInfo)
    }

}
