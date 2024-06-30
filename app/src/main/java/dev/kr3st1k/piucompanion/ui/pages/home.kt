package dev.kr3st1k.piucompanion.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.ui.components.home.HomeBottomBar
import kotlinx.coroutines.launch


val topLevelDestinations = listOf(
    TopLevelDestination(
        route = Screen.BestUserPage.route,
        selectedIcon = Icons.Filled.FormatListNumbered,
        unselectedIcon = Icons.Outlined.FormatListNumbered,
        iconText = "Best Scores"
    ),
    TopLevelDestination(
        route = Screen.HistoryPage.route,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        iconText = "History"
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
fun HomeScreen() {


    val navControllerLocal = rememberNavController()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navControllerLocal.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        topBar = {
            if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        if (currentRoute in homeDestinations.map { it.route })
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
                    modifier = Modifier.clickable(
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
            if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                HomeBottomBar(destinations = topLevelDestinations,
                    onListUp = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    currentDestination = navControllerLocal.currentBackStackEntryAsState().value?.destination,
                    onNavigateToDestination = {

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
                .padding(it),
            navController = navControllerLocal,
            listState = listState
        )
    }
}
