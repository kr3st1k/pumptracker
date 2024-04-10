package dev.kr3st1k.piucompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.R
import dev.kr3st1k.piucompanion.ui.components.home.HomeBottomBar


val topLevelDestinations = listOf(
    TopLevelDestination(
        route = Screen.BestUserPage.route,
        selectedIcon = R.drawable.baseline_format_list_numbered_24,
        unselectedIcon = R.drawable.baseline_format_list_numbered_24,
        iconText = "Best Scores"
    ),
    TopLevelDestination(
        route = Screen.HistoryPage.route,
        selectedIcon = R.drawable.baseline_history_24,
        unselectedIcon = R.drawable.baseline_history_24,
        iconText = "History"
    ),
    TopLevelDestination(
        route = Screen.UserPage.route,
        selectedIcon = R.drawable.baseline_person_24,
        unselectedIcon = R.drawable.baseline_person_24,
        iconText = "Player"
    )
)

public val homeDestinations = listOf(
    TopLevelDestination(
        route = Screen.NewsPage.route,
        selectedIcon = R.drawable.baseline_newspaper_24,
        unselectedIcon = R.drawable.baseline_newspaper_24,
        iconText = "News",
        summary = "Read latest news about Pump It Up"
    ),
    TopLevelDestination(
        route = Screen.NewsPage.route,
        selectedIcon = R.drawable.baseline_leaderboard_24,
        unselectedIcon = R.drawable.baseline_leaderboard_24,
        iconText = "Leaderboard",
        summary = "Let's see who sniped FEFEMZ's scores"
    ),
    TopLevelDestination(
        route = Screen.NewsPage.route,
        selectedIcon = R.drawable.baseline_dvr_24,
        unselectedIcon = R.drawable.baseline_dvr_24,
        iconText = "Title Changer",
        summary = "Select the most beautiful title available"
    ),
    TopLevelDestination(
        route = Screen.NewsPage.route,
        selectedIcon = R.drawable.baseline_account_circle_24,
        unselectedIcon = R.drawable.baseline_account_circle_24,
        iconText = "Avatar Shop",
        summary = "Wow! Nice picture!"
    ),
    TopLevelDestination(
        route = Screen.SettingsPage.route,
        selectedIcon = R.drawable.baseline_settings_24,
        unselectedIcon = R.drawable.baseline_settings_24,
        iconText = "Settings",
        summary = "He he"
    )
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {


    val navControllerLocal = rememberNavController()

    val currentRoute = navControllerLocal.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        if (currentRoute in homeDestinations.map { it.route })
                            IconButton(onClick = {
                                navControllerLocal.popBackStack()
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
                })
            }
        },
        bottomBar = {
            if (currentRoute in topLevelDestinations.map { it.route } || currentRoute in homeDestinations.map { it.route }) {
                HomeBottomBar(destinations = topLevelDestinations,
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            HomeNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                navController = navControllerLocal
            )
        }
    }
}
