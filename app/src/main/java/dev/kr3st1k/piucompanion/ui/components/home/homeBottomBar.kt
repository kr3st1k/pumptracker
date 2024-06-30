package dev.kr3st1k.piucompanion.ui.components.home

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.kr3st1k.piucompanion.ui.pages.TopLevelDestination
import kotlinx.coroutines.Job

@Composable
fun HomeBottomBar(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (route: String) -> Unit,
    onListUp: () -> Job
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
                onClick = {
                    if (selected)
                        onListUp()
                    else
                        onNavigateToDestination(destination.route)
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon
                        else destination.unselectedIcon,
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