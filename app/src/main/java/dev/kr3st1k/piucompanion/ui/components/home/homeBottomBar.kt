package dev.kr3st1k.piucompanion.ui.components.home

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.kr3st1k.piucompanion.ui.screens.TopLevelDestination

@Composable
fun HomeBottomBar(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (route: String) -> Unit
) {

    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) ImageVector.vectorResource(destination.selectedIcon) else ImageVector.vectorResource(
                            destination.unselectedIcon
                        ),
                        contentDescription = null
                    )
                },
//                label = {
//                    Text(
//                        text = destination.iconText
//                    )
//                }
            )
        }
    }
}