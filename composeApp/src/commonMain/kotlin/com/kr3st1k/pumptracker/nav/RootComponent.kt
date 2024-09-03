package com.kr3st1k.pumptracker.nav

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.nav.auth.AuthComponent
import com.kr3st1k.pumptracker.nav.authloading.AuthLoadingComponent
import com.kr3st1k.pumptracker.nav.avatar.AvatarShopComponent
import com.kr3st1k.pumptracker.nav.best.BestUserComponent
import com.kr3st1k.pumptracker.nav.history.HistoryComponent
import com.kr3st1k.pumptracker.nav.news.NewsComponent
import com.kr3st1k.pumptracker.nav.pumbility.PumbilityComponent
import com.kr3st1k.pumptracker.nav.settings.SettingsComponent
import com.kr3st1k.pumptracker.nav.title.TitleShopComponent
import com.kr3st1k.pumptracker.nav.user.UserComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentComponent: ComponentContext
) : ComponentContext by componentComponent {
    private val stack = StackNavigation<TopLevelConfiguration>()
    private val loginManager = LoginManager()

    val childStack = childStack(
        source = stack,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = if (loginManager.hasLoginData()) {
            TopLevelConfiguration.AuthLoadingPage
        } else {
            TopLevelConfiguration.AuthPage
        },
        childFactory = ::createChild,
        handleBackButton = true
    )

    fun popBack() {
        stack.pop()
    }

    fun navigateTo(configuration: TopLevelConfiguration) {
        stack.push(configuration)
    }

    fun navigateAndReset(configuration: TopLevelConfiguration) {
        stack.replaceAll(configuration)
    }

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            is TopLevelConfiguration.AuthPage -> TopLevelChild.AuthPage(
                AuthComponent(
                    navigateToScreen = { stack.replaceAll(TopLevelConfiguration.HistoryPage) },
                    context
                )
            )

            is TopLevelConfiguration.AuthLoadingPage -> TopLevelChild.AuthLoadingPage(
                AuthLoadingComponent(
                    componentContext = context,
                    navigateTo = { stack.replaceAll(it) }
                )
            )

            is TopLevelConfiguration.AvatarShopPage -> TopLevelChild.AvatarShopPage(
                AvatarShopComponent(
                    navigateToLogin = { stack.replaceAll(TopLevelConfiguration.HistoryPage) },
                    context
                )
            )

            is TopLevelConfiguration.BestUserPage -> TopLevelChild.BestUserPage(
                BestUserComponent(
                    navigateTo = ::navigateTo,
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthLoadingPage)
                    },
                    context
                )
            )

            is TopLevelConfiguration.HistoryPage -> TopLevelChild.HistoryPage(
                HistoryComponent(
                    navigateTo = ::navigateTo,
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthLoadingPage)
                    },
                    context
                )
            )

            is TopLevelConfiguration.NewsPage -> TopLevelChild.NewsPage(
                NewsComponent(
                    context
                )
            )

            is TopLevelConfiguration.PumbilityPage -> TopLevelChild.PumbilityPage(
                PumbilityComponent(
                    navigateTo = ::navigateTo,
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthLoadingPage)
                    },
                    context
                )
            )

            is TopLevelConfiguration.SettingsPage -> TopLevelChild.SettingsPage(
                SettingsComponent(
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthPage)
                    },
                    context
                )
            )

            is TopLevelConfiguration.TitleShopPage -> TopLevelChild.TitleShopPage(
                TitleShopComponent(
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthPage)
                    },
                    context
                )
            )

            is TopLevelConfiguration.UserPage -> TopLevelChild.UserPage(
                UserComponent(
                    navigateTo = ::navigateTo,
                    navigateToLogin = {
                        stack.replaceAll(TopLevelConfiguration.AuthLoadingPage)
                    },
                    context
                )
            )
        }
    }

    sealed class TopLevelChild(open val component: ComponentContext) {
        data class AuthLoadingPage(override val component: AuthLoadingComponent) : TopLevelChild(component)
        data class AuthPage(override val component: AuthComponent) : TopLevelChild(component)
        data class BestUserPage(override val component: BestUserComponent) : TopLevelChild(component)
        data class AvatarShopPage(override val component: AvatarShopComponent) : TopLevelChild(component)
        data class HistoryPage(override val component: HistoryComponent) : TopLevelChild(component)
        data class PumbilityPage(override val component: PumbilityComponent) : TopLevelChild(component)
        data class TitleShopPage(override val component: TitleShopComponent) : TopLevelChild(component)
        data class NewsPage(override val component: NewsComponent) : TopLevelChild(component)
        data class UserPage(override val component: UserComponent) : TopLevelChild(component)
        data class SettingsPage(override val component: SettingsComponent) : TopLevelChild(component)
    }

    @Serializable
    sealed class TopLevelConfiguration {
        @Serializable
        data object AuthLoadingPage : TopLevelConfiguration()

        @Serializable
        data object NewsPage : TopLevelConfiguration()

        @Serializable
        data object UserPage : TopLevelConfiguration()

        @Serializable
        data object AuthPage : TopLevelConfiguration()

        @Serializable
        data object HistoryPage : TopLevelConfiguration()

        @Serializable
        data object BestUserPage : TopLevelConfiguration()

        @Serializable
        data object PumbilityPage : TopLevelConfiguration()

        @Serializable
        data object SettingsPage : TopLevelConfiguration()

        @Serializable
        data object AvatarShopPage : TopLevelConfiguration()

        //        data class Screen(val text: String) : Configuration()
        @Serializable
        data object TitleShopPage : TopLevelConfiguration()
    }
}