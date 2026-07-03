package com.sportz.base.di

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.sportz.base.helper.BaseConfigContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCustomTabIntent(
        baseConfigContract: BaseConfigContract,
        @ApplicationContext context: Context
    ): CustomTabsIntent {
        val toolbarColor = ContextCompat.getColor(context, baseConfigContract.chromeToolbarColor())
        return CustomTabsIntent.Builder().setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder().setToolbarColor(toolbarColor).build()
        ).build()
    }

}