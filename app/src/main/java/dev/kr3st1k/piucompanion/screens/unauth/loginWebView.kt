package dev.kr3st1k.piucompanion.screens.unauth

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.screens.components.MyAlertDialog
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.screens.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginWebViewScreen(navController: NavController) {

    val scope = rememberCoroutineScope()

    var webView: WebView? by remember { mutableStateOf(null) }
    var showDialog by remember { mutableStateOf(false) }

    val pref = PreferencesManager(LocalContext.current)
    Column {

        TopAppBar(title = { Text(text = "Authorize in site") },

            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ), actions = {
                IconButton(onClick = {

                    val cookieManager = CookieManager.getInstance()

                    val cookies = cookieManager.getCookie("https://piugame.com")
                    if (cookies == null) {
                        showDialog = true
                    } else {
                        scope.launch {
                            if (cookies.contains("nullsid") || cookies.split(";").size >= 5) {
                                println(cookies)
                                val t = webView?.settings?.userAgentString?.let {
                                    RequestHandler.checkIfLoginSuccess(
                                        cookies,
                                        it
                                    )
                                }

                                if (t!!) {
                                    pref.saveData("cookies", cookies)
                                    webView?.settings?.let {
                                        pref.saveData(
                                            "ua",
                                            it.userAgentString
                                        )
                                    }
                                    navController.navigate(Screen.HomeScreen.route)
                                } else {
                                    showDialog = true
                                }

                                println(t)
                            }
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                }
            }
        )

        MyAlertDialog(
            showDialog = showDialog,
            title = "Login failed!",
            content = "Maybe try authorize in site?",
            onDismiss = { showDialog = false })

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context)
            },

            update = { view ->
                WebView.setWebContentsDebuggingEnabled(true)
                webView=view.apply {
                    settings.javaScriptEnabled=true
                    webViewClient=object:WebViewClient()
                    {
                        override fun onPageFinished(view: WebView, url: String)
                        {
                            super.onPageFinished(view, url)
//                            view.evaluateJavascript("if (!window.eruda) {let parent = document.head || document.documentElement; let script = parent.appendChild(document.createElement('script')); script.src = 'https://cdn.jsdelivr.net/npm/eruda'; script.onload = () => eruda.init();}", null);
                            scope.launch {
                                val cookieManager = CookieManager.getInstance()

                                val cookies = cookieManager.getCookie("https://piugame.com")
                                if (cookies != null) {
                                    if (cookies.contains("nullsid")  || cookies.split(";").size >= 5) {
                                        println(cookies)
                                        val t = webView?.settings?.userAgentString?.let {

                                            RequestHandler.checkIfLoginSuccess(
                                                cookies,
                                                it
                                            )
                                        }

                                        if (t!!) {
                                            pref.saveData("cookies", cookies)
                                            webView?.settings?.let {
                                                pref.saveData(
                                                    "ua",
                                                    it.userAgentString
                                                )
                                            }
                                            navController.navigate(Screen.HomeScreen.route)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    loadUrl("https://piugame.com/login.php")
                }
            }
        )
    }
}