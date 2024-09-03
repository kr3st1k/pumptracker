package com.kr3st1k.pumptracker.ui.components.home

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.TopLevelDestination
import kotlinx.coroutines.Job

@Composable
fun HomeBottomBar(
    destinations: List<TopLevelDestination>,
    currentDestination: RootComponent.TopLevelConfiguration,
    onNavigateToDestination: (route: RootComponent.TopLevelConfiguration) -> Unit,
    onListUp: () -> Job
) {

    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        destinations.forEach { destination ->
            val selected =
                currentDestination == destination.route
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