package dev.kr3st1k.piucompanion.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.R
import dev.kr3st1k.piucompanion.objects.TopLevelDestination
import dev.kr3st1k.piucompanion.screens.HomeNavHost
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.home.HomeBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = Screen.NewsPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_newspaper_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_newspaper_24),
            iconText = "News"
        ),TopLevelDestination(
            route = Screen.BestUserPage.route,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_format_list_numbered_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.baseline_format_list_numbered_24),
            iconText = "Best Scores"
        ),TopLevelDestination(
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

    val showBottomBar = remember { mutableStateOf(true) }
    val title = remember {
        mutableStateOf("News")
    }
    val navControllerLocal = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = title.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            })
        },
        bottomBar = {
            if (showBottomBar.value) {
                HomeBottomBar(destinations = topLevelDestinations,
                    currentDestination = navControllerLocal.currentBackStackEntryAsState().value?.destination,
                    onNavigateToDestination = {

                        val tt = topLevelDestinations.find { t -> t.route == it };

                        if (tt != null) {
                            title.value = tt.iconText
                        }

                        navControllerLocal.navigate(it) {
                            popUpTo(navControllerLocal.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
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
                navControllerGlobal = navController,
                startDestination = Screen.NewsPage.route
            )

        }
    }
}
