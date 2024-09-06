package com.kr3st1k.pumptracker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.ChildStack
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.helper.IUpdateList
import com.kr3st1k.pumptracker.nav.homeDestinations
import com.kr3st1k.pumptracker.nav.topLevelDestinations
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DecoratedWindowScope.TitleBarView(
    titleBarStyle: TitleBarStyle,
    rootComponent: RootComponent,
    stack: ChildStack<RootComponent.TopLevelConfiguration, RootComponent.TopLevelChild>,
    isDark: Boolean
) {
    val title = topLevelDestinations.find { t ->
        t.route == stack.active.configuration
    }?.iconText ?: homeDestinations.find { t ->
        t.route == stack.active.configuration
    }?.iconText

    TitleBar(Modifier.newFullscreenControls(), style = titleBarStyle) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            if (homeDestinations.any { it.route == stack.active.configuration })
                Tooltip({
                    Text("Back")
                }) {
                    IconButton(
                        rootComponent::popBack,
                        Modifier.size(40.dp).padding(5.dp)) {
                        Icon(if (isDark) painterResource("drawable/left_dark.svg") else painterResource("drawable/left.svg"), "Back")
                    }
                }
            if (stack.active.instance.component is IUpdateList)
                Tooltip({
                    Text("Refresh the data")
                }) {
                    IconButton(
                        rootComponent::refreshValues,
                        Modifier.size(40.dp).padding(5.dp)) {
                        Icon(if (isDark) painterResource("drawable/refresh_dark.xml") else painterResource("drawable/refresh.xml"), "Update Page")
                    }
                }
        }

        Row(Modifier.align(Alignment.CenterHorizontally)) {
            Icon(painterResource("drawable/icon.png"), null, modifier = Modifier.size(24.dp).padding(end = 4.dp))
            Row(
                Modifier.align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PumpTracker${title?.let { " | $it" } ?: ""}")
            }
        }
    }

}
