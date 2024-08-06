package com.kr3st1k.pumptracker.core.db.data.title

import com.kr3st1k.pumptracker.core.db.data.score.Score
import com.kr3st1k.pumptracker.core.db.data.title.PhoenixCategoryItem

open class PhoenixTitle(
    val name: String,
    val category: PhoenixCategoryItem = PhoenixCategoryItem.Misc
) {

    open fun completionProgress(scores: List<Score>): Float {
        return 0F
    }

    open fun completionProgressValue(scores: List<Score>): String {
        return "0"
    }

}