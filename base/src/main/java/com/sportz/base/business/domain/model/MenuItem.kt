package com.sportz.base.business.domain.model


data class LangMenuItem(
    val lang: String, val menu: List<MenuItem> = emptyList()
)

data class MenuItem(
    val menu_id: Int,
    val menu_title: String?,
    val screen_name: String?,
    val order: Int,
    val is_visible: Boolean?,
    val is_bottom_bar: Boolean?,
    val has_interaction: Boolean?,
    val in_app_browser: Boolean?,
    val is_webview: Boolean?,
    val is_external_webview: Boolean?,
    val menu_icon: String?,
    val webview_url: String?,
    var is_selected: Boolean = false,
    var is_web_auth: Boolean,
    val is_timestamp: Boolean = false,
    val show_sponsor: Boolean = false,
    val sponsor_text: String,
    val sponsor_image: String
)