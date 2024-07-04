package dev.kr3st1k.piucompanion.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.ui.components.home.HomeBottomBar
import kotlinx.coroutines.launch


val topLevelDestinations = listOf(
    TopLevelDestination(
        route = Screen.BestUserPage.route,
        selectedIcon = Icons.Filled.FormatListNumbered,
        unselectedIcon = Icons.Outlined.FormatListNumbered,
        iconText = "Best Scores",
        availableAtOffline = true
    ),
    TopLevelDestination(
        route = Screen.HistoryPage.route,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        iconText = "History",
        availableAtOffline = true
    ),
    TopLevelDestination(
        route = Screen.UserPage.route,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconText = "Player"
    )
)

public val homeDestinations = listOf(
    TopLevelDestination(
        route = Screen.NewsPage.route,
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Filled.Newspaper,
        iconText = "News",
        summary = "Read latest news about Pump It Up"
    ),
    TopLevelDestination(
        route = Screen.PumbilityPage.route,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Filled.Analytics,
        iconText = "PUMBILITY",
        summary = "Best 50 scores"
    ),
//    TopLevelDestination(
//        route = Screen.NewsPage.route,
//        selectedIcon = Icons.Filled.Leaderboard,
//        unselectedIcon = Icons.Filled.Leaderboard,
//        iconText = "Leaderboard",
//        summary = "Let's see who sniped FEFEMZ's scores"
//    ),
//    TopLevelDestination(
//        route = Screen.NewsPage.route,
//        selectedIcon = Icons.AutoMirrored.Filled.Dvr,
//        unselectedIcon = Icons.AutoMirrored.Filled.Dvr,
//        iconText = "Title Changer",
//        summary = "Select the most beautiful title available"
//    ),
    TopLevelDestination(
        route = Screen.AvatarShopPage.route,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Filled.AccountCircle,
        iconText = "Avatar Shop",
        summary = "Wow! Nice picture!"
    ),
    TopLevelDestination(
        route = Screen.SettingsPage.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Filled.Settings,
        iconText = "Settings",
        summary = "He he"
    )
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(showNavigationRail: Boolean) {


    val navControllerLocal = rememberNavController()

    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navControllerLocal.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        topBar = {
            if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        if (!showNavigationRail && (currentRoute in homeDestinations.map { it.route }))
                            IconButton(onClick = {
                                navControllerLocal.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                    },
                    title = {
                        Text(
                            text = topLevelDestinations.find { t ->
                                t.route == (currentRoute ?: "")
                            }?.iconText ?: homeDestinations.find { t ->
                                t.route == (currentRoute ?: "")
                            }?.iconText ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    },
                    expandedHeight = if (showNavigationRail) 42.dp else 64.dp,
                    modifier = Modifier
                        .padding(start = if (showNavigationRail && (currentRoute in topLevelDestinations.map { it2 -> it2.route } || currentRoute in homeDestinations.map { it2 -> it2.route })) 80.dp else 0.dp)

                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        )
                )
            }
        },
        bottomBar = {
            if (!showNavigationRail)
                if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                    HomeBottomBar(
                        destinations = if (InternetManager().hasInternetStatus()) topLevelDestinations else topLevelDestinations.filter { it.availableAtOffline },
                        onListUp = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        currentDestination = navControllerLocal.currentBackStackEntryAsState().value?.destination,
                        onNavigateToDestination = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                            navControllerLocal.navigate(it) {
                                popUpTo(navControllerLocal.currentDestination!!.route!!) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
        }
    ) {
        HomeNavHost(
            modifier = Modifier
                .padding(it)
                .padding(start = if (showNavigationRail && (currentRoute in topLevelDestinations.map { it2 -> it2.route } || currentRoute in homeDestinations.map { it2 -> it2.route })) 80.dp else 0.dp),
            navController = navControllerLocal,
            listState = listState
        )
        if (showNavigationRail && (currentRoute in topLevelDestinations.map { it2 -> it2.route } || currentRoute in homeDestinations.map { it2 -> it2.route })) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .offset(x = (-1).dp)
            ) {
                NavigationRail(
                    header = {
                        if (currentRoute in homeDestinations.map { curr -> curr.route })
                            IconButton(onClick = {
                                navControllerLocal.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
                    ) {
                        var dests = topLevelDestinations
                        if (!InternetManager().hasInternetStatus())
                            dests = dests.filter { route -> route.availableAtOffline }
                        dests.forEach { destination ->
                            val selected =
                                navControllerLocal.currentBackStackEntryAsState().value?.destination?.hierarchy?.any { it.route == destination.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                    navControllerLocal.navigate(destination.route) {
                                        popUpTo(navControllerLocal.currentDestination!!.route!!) {
                                            inclusive = true
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) destination.selectedIcon
                                        else destination.unselectedIcon,
                                        contentDescription = null
                                    )
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}
