package com.sportz.si_matchcenter.di

import com.sportz.base.helper.KtorServiceHelper
import com.sportz.si_matchcenter.data.service.MatchCenterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ServiceModule {

    @Provides
    @ViewModelScoped
    fun provideMatchCenterService(serviceHelper: KtorServiceHelper): MatchCenterService {
        return MatchCenterService(serviceHelper)
    }
}
