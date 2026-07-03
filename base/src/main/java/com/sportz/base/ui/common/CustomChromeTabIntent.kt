package com.sportz.base.ui.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomChromeTabIntent @Inject constructor(
    private val customTabsIntent: CustomTabsIntent,
) {

    fun openCustomTab(
        activity: Activity?,
        url: String,
    ) {
        if (url.isBlank()) return

        val uri = Uri.parse(url)
        val packageName = "com.android.chrome"
        customTabsIntent.intent.setPackage(packageName)
        try {
            customTabsIntent.launchUrl(activity ?: return, uri)
        } catch (e: Exception) {
            try {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
            }
        }
    }

}