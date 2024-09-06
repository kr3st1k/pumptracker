package com.kr3st1k.pumptracker.nav.helper

import com.arkivanov.decompose.value.Value

interface IScrollToUp {
    val isScrollable: Value<Boolean>

    fun scrollUp()
}
