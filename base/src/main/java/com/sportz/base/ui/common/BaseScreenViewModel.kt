package com.sportz.base.ui.common

import com.sportz.base.business.domain.model.MenuItem
import com.sportz.base.helper.BaseConfigContract

abstract class BaseScreenViewModel<S: UiState, I: MviIntent>(
    private val baseConfigContract: BaseConfigContract?
) : BaseViewModel<S, I>() {

    fun getMenuItem(menuID: Int): MenuItem? {
        return baseConfigContract?.getBottomMenuItems().orEmpty().find { it.menu_id == menuID }
    }
}
