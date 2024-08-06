package com.kr3st1k.pumptracker.core.network.data

data class LoadableList<T>(
    val res: MutableList<T>,
    val isLoadMore: Boolean,
    val lastPageNumber: Int = 1
)
