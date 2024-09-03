import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.kr3st1k.pumptracker.*
import com.kr3st1k.pumptracker.core.db.AppDatabase
import com.kr3st1k.pumptracker.di.ManagerModules
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.navigateUp
import com.kr3st1k.pumptracker.nav.refreshFunction
import com.kr3st1k.pumptracker.platform.getSNMacintosh
import com.kr3st1k.pumptracker.platform.getSNNix
import com.kr3st1k.pumptracker.platform.getSNWindows
import com.lizowzskiy.accents.getAccentColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.lightWithLightHeader
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarStyle
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.awt.Dimension

var sn: String? = null

fun main() {
    startKoin {
        modules(
            ManagerModules.loginModule,
            ManagerModules.bgModule,
            ManagerModules.internetModule,
            module {
                single<AppDatabase> {
                    createDatabaseBuilder()
                        .setDriver(BundledSQLiteDriver())
                        .setQueryCoroutineContext(Dispatchers.IO)
                        .build()
                }
            }
        )
    }

    val platform = getPlatform().name.lowercase()

    sn = if (platform.contains("win"))
        getSNWindows()
    else if (platform.contains("mac") || platform.contains("darwin"))
        getSNMacintosh()
    else if (platform.contains("nux"))
        getSNNix()
    else
        "trollface"

    sn = sn?.substring(0, if (sn!!.length >= 16) 16 else sn!!.length)

    while (sn?.length!! < 16)
        sn += "0"

    val isUpdateRequired =  mutableStateOf(false)
    val updateLink = mutableStateOf("")
    val lifecycle = LifecycleRegistry()
    application {

        val scope = rememberCoroutineScope()

        scope.launch {
//            val getUpdate = NetworkRepositoryImpl.getGithubUpdateInfo()
            isUpdateRequired.value = false
        }


        val windowState = rememberWindowState(width = 1080.dp, height = 620.dp)

        val systemDesktopColor = try {
            getAccentColor()
        } catch (e: Exception) {
            null
        }

        val composeColor = systemDesktopColor?.let {
            Color(red = it.r.toInt(), green = it.g.toInt(), blue = it.b.toInt())
        } ?: Color.Unspecified

        val isDark = ((isSystemDefault.value && isSystemInDarkTheme()) || isDarkTheme.value)
        LifecycleController(lifecycle, windowState)
        IntUiTheme(
            theme = if (isDark) JewelTheme.darkThemeDefinition() else JewelTheme.lightThemeDefinition(),
            styling = if (isDark) ComponentStyling.default()
                .decoratedWindow(titleBarStyle = TitleBarStyle.dark()) else ComponentStyling.default()
                .decoratedWindow(titleBarStyle = TitleBarStyle.lightWithLightHeader()),
        ) {
            DecoratedWindow(
                icon = painterResource("drawable/icon.png"),
                title = "PumpTracker",
                onCloseRequest = ::exitApplication,
                state = windowState,
                onKeyEvent = {
                    if (
                        it.key == Key.Escape &&
                        navigateUp != null
                    ) {
                        navigateUp!!()
                        true
                    } else if(
                        it.key == Key.F5 &&
                        refreshFunction.value != null
                    ) {
                        refreshFunction.value?.let { it1 -> it1() }
                        true
                    } else {
                        false
                    }
                }
            ) {
                val root = remember {
                    RootComponent(DefaultComponentContext(lifecycle))
                }
                this.window.minimumSize = Dimension(1100, 620)
                TitleBarView(
                    titleBarStyle = if (isDark) TitleBarStyle.dark(colors = TitleBarColors.dark(inactiveBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.98F), backgroundColor = MaterialTheme.colorScheme.onBackground)) else TitleBarStyle.lightWithLightHeader(),
                    isDark = isDark
                )
                App(
                    root = root,
                    isUpdateRequired = isUpdateRequired,
                    updateLink = updateLink,
                    seedColor = composeColor
                )
            }
        }
    }
}