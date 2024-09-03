package com.kr3st1k.pumptracker.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Dvr
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.nav.auth.AuthComponentImpl
import com.kr3st1k.pumptracker.nav.authloading.AuthLoadingComponentImpl
import com.kr3st1k.pumptracker.nav.avatar.AvatarShopComponentImpl
import com.kr3st1k.pumptracker.nav.best.BestUserComponentImpl
import com.kr3st1k.pumptracker.nav.history.HistoryComponentImpl
import com.kr3st1k.pumptracker.nav.news.NewsComponentImpl
import com.kr3st1k.pumptracker.nav.pumbility.PumbilityComponentImpl
import com.kr3st1k.pumptracker.nav.settings.SettingsComponentImpl
import com.kr3st1k.pumptracker.nav.title.TitleShopComponentImpl
import com.kr3st1k.pumptracker.nav.user.UserComponentImpl
import com.kr3st1k.pumptracker.ui.components.home.HomeBottomBar
import kotlinx.coroutines.launch

val topLevelDestinations = listOf(
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.BestUserPage,
        selectedIcon = Icons.Filled.FormatListNumbered,
        unselectedIcon = Icons.Outlined.FormatListNumbered,
        iconText = "Best Scores"
    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.HistoryPage,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        iconText = "History"
    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.UserPage,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconText = "Player"
    )
)

public val homeDestinations = listOf(
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.NewsPage,
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Filled.Newspaper,
        iconText = "News",
        summary = "Read latest news about Pump It Up"
    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.PumbilityPage,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Filled.Analytics,
        iconText = "PUMBILITY",
        summary = "Best 50 scores",
        availableAtOffline = true
    ),
//    TopLevelDestination(
//        route = Screen.NewsPage.route,
//        selectedIcon = Icons.Filled.Leaderboard,
//        unselectedIcon = Icons.Filled.Leaderboard,
//        iconText = "Leaderboard",
//        summary = "Let's see who sniped FEFEMZ's scores"
//    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.TitleShopPage,
        selectedIcon = Icons.AutoMirrored.Filled.Dvr,
        unselectedIcon = Icons.AutoMirrored.Filled.Dvr,
        iconText = "Title Shop",
        summary = "Rizz your title"
    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.AvatarShopPage,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Filled.AccountCircle,
        iconText = "Avatar Shop",
        summary = "Wow! Nice picture!"
    ),
    TopLevelDestination(
        route = RootComponent.TopLevelConfiguration.SettingsPage,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Filled.Settings,
        iconText = "Settings",
        summary = "He he",
        availableAtOffline = true
    )
)

var refreshFunction: MutableState<(() -> Unit)?> = mutableStateOf(null)

var currentPage: String? = null

var navigateUp: (() -> Unit)? = null
var isOffline = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootComponentImpl(rootComponent: RootComponent, showNavigationRail: Boolean) {
    val stack by rootComponent.childStack.subscribeAsState()
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val currentInstance = stack.items.last()

    currentPage = topLevelDestinations.find { t ->
        t.route == (currentInstance.configuration ?: "")
    }?.iconText ?: homeDestinations.find { t ->
        t.route == (currentInstance.configuration ?: "")
    }?.iconText

    Scaffold(
        topBar = {
            if ((currentInstance.configuration in topLevelDestinations.map { it.route } || currentInstance.configuration in homeDestinations.map { it.route }) && getPlatform().type == "Mobile") {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        if (!showNavigationRail && (currentInstance.configuration in homeDestinations.map { it.route }))
                            IconButton(onClick = {
                                rootComponent.popBack()
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
                                t.route == (currentInstance.configuration ?: "")
                            }?.iconText ?: homeDestinations.find { t ->
                                t.route == (currentInstance.configuration ?: "")
                            }?.iconText ?: "",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                    },
                    modifier = Modifier
                        .padding(start = if (showNavigationRail && (currentInstance.configuration in topLevelDestinations.map { it2 -> it2.route } || currentInstance.configuration in homeDestinations.map { it2 -> it2.route })) 80.dp else 0.dp)

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
                if (currentInstance.configuration in topLevelDestinations.map { it.route } || currentInstance.configuration in homeDestinations.map { it.route }) {
                    HomeBottomBar(
                        destinations = topLevelDestinations,
                        onListUp = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        currentDestination = stack.items.last().configuration,
                        onNavigateToDestination = {
                            if (stack.items.last().configuration == it) {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            } else {
                                rootComponent.navigateAndReset(it)
                            }
                        }
                    )
                }
        }
    ) {
        val goBack = { rootComponent.popBack() }
        navigateUp =
            if (stack.items.last().configuration in homeDestinations.map { curr -> curr.route }) goBack else null

        if (showNavigationRail && (currentInstance.configuration in topLevelDestinations.map { it2 -> it2.route } || currentInstance.configuration in homeDestinations.map { it2 -> it2.route })) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .offset(x = (-1).dp)
            ) {
                NavigationRail(
                    header = {
                        if (getPlatform().type == "Mobile")
                            if (currentInstance.configuration in homeDestinations.map { curr -> curr.route })
                                IconButton(onClick = {
                                    rootComponent.popBack()
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
                        topLevelDestinations.forEach { destination ->
                            val selected = stack.items.last().configuration == destination.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    if (currentInstance.configuration == destination.route) {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(0)
                                        }
                                    } else {
                                        rootComponent.navigateAndReset(destination.route)
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
        Row(
            modifier = Modifier
                .padding(it)
                .padding(start = if (showNavigationRail && (stack.items.last().configuration in topLevelDestinations.map { it2 -> it2.route } || stack.items.last().configuration in homeDestinations.map { it2 -> it2.route })) 80.dp else 0.dp)
                .padding(top = if (getPlatform().type == "Desktop") 8.dp else 0.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Children(
                    animation = stackAnimation(fade()),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    stack = stack
                ) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.TopLevelChild.HistoryPage -> HistoryComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.AuthLoadingPage -> AuthLoadingComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.AuthPage -> AuthComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.AvatarShopPage -> AvatarShopComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.BestUserPage -> BestUserComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.NewsPage -> NewsComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.PumbilityPage -> PumbilityComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.SettingsPage -> SettingsComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.TitleShopPage -> TitleShopComponentImpl(instance.component)
                        is RootComponent.TopLevelChild.UserPage -> UserComponentImpl(instance.component)
                    }

                }
            }
        }

    }


}