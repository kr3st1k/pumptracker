package com.kr3st1k.pumptracker.nav

import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelDestination(
    val route: RootComponent.TopLevelConfiguration,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val showTitle: Boolean = true,
    val summary: String? = null,
    val availableAtOffline: Boolean = false
)
