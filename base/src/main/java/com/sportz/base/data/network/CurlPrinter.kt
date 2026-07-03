package com.sportz.base.data.network

import android.util.Log
import com.sportz.base.BuildConfig
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.content.OutgoingContent

/**
 * A Ktor plugin that prints the CURL command for every request in debug mode.
 */
val CurlPrinterPlugin = createClientPlugin("CurlPrinterPlugin") {
    onRequest { request, content ->
        if (BuildConfig.DEBUG) {
            try {
                val outgoingContent = content as? OutgoingContent
                val curl = toCurl(request, outgoingContent)
                Log.d("CurlPrinter", curl)
            } catch (e: Exception) {
                Log.e("CurlPrinter", "Failed to print curl", e)
            }
        }
    }
}

private fun toCurl(request: HttpRequestBuilder, content: OutgoingContent?): String {
    val curl = StringBuilder("curl -X ${request.method.value} ")
    curl.append("'${request.url.build()}' ")

    request.headers.entries().forEach { (key, values) ->
        values.forEach { curl.append("-H '$key: $it' ") }
    }

    if (content is OutgoingContent.ByteArrayContent) {
        try {
            curl.append("-d '${String(content.bytes())}'")
        } catch (e: Exception) {
            // Ignore if body cannot be read
        }
    }

    return curl.toString()
}
