package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.R
import dev.kr3st1k.piucompanion.core.objects.TopLevelDestination
import dev.kr3st1k.piucompanion.ui.components.home.HomeBottomBar
import dev.kr3st1k.piucompanion.ui.screens.HomeNavHost
import dev.kr3st1k.piucompanion.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = Screen.NewsPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_newspaper_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_newspaper_24),
            iconText = "News"
        ),
        TopLevelDestination(
            route = Screen.BestUserPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_format_list_numbered_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_format_list_numbered_24),
            iconText = "Best Scores"
        ),
        TopLevelDestination(
            route = Screen.HistoryPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_history_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_history_24),
            iconText = "History"
        ),
        TopLevelDestination(
            route = Screen.UserPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_person_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_person_24),
            iconText = "Player"
        ),

    )

    val showBottomBar = rememberSaveable { mutableStateOf(false) }

    val navControllerLocal = rememberNavController()

    Scaffold(
        topBar = {
            if (showBottomBar.value) {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = topLevelDestinations.find { t ->
                            t.route == (navControllerLocal.currentBackStackEntryAsState().value?.destination?.route
                                ?: "")
                        }?.iconText ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                })
            }
        },
        bottomBar = {
            if (showBottomBar.value) {
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
                navController = navControllerLocal,
                onNavigateShowBottomBar = { showBottomBar.value = true },
                onNavigateNotShowBottomBar = { showBottomBar.value = false }
            )
        }
    }
}
