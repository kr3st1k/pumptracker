package dev.kr3st1k.piucompanion.screens.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Base64
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.components.MyAlertDialog
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.CookieData
import dev.kr3st1k.piucompanion.screens.Screen
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.HttpUrl


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginWebViewScreen(navController: NavController) {

    val scope = rememberCoroutineScope()

    var webView: WebView? by remember { mutableStateOf(null) }
    var showDialog by remember { mutableStateOf(false) }

    val darkTheme = isSystemInDarkTheme() // or use your theme state
    val backgroundColor = if (darkTheme) Color.Black else Color.White

    val isLoading = remember { mutableStateOf(true) }

    val pref = PreferencesManager(LocalContext.current)
    Column(
        modifier = Modifier.background(color = backgroundColor)
    ) {

        TopAppBar(title = { Text(text = "Authorize in site") },

            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ), actions = {
                IconButton(onClick = {

                    val cookieManager = CookieManager.getInstance()

                    var cookies = cookieManager.getCookie("https://am-pass.net")

                    if (cookies == null) {
                        showDialog = true
                    } else {
                        scope.launch {
                            if (cookies.contains("nullsid") || cookies.split(";").size >= 5) {
                                val uri = HttpUrl.Builder()
                                    .scheme("https")
                                    .host("am-pass.net")
                                    .build();
                                val uri2 = HttpUrl.Builder()
                                    .scheme("https")
                                    .host("www.piugame.com")
                                    .build();
                                val uri3 = HttpUrl.Builder()
                                    .scheme("https")
                                    .host("api.am-pass.net")
                                    .build();
                                val parsedCookies =
                                    cookies.split(";").map { Cookie.parse(uri, it) }.toMutableList()
                                parsedCookies += cookies.split(";").map { Cookie.parse(uri2, it) }
                                cookies = cookieManager.getCookie("https://api.am-pass.net")
                                parsedCookies += cookies.split(";").map { Cookie.parse(uri3, it) }
                                val base64Json = parsedCookies.toJson().toBase64()
                                val t = webView?.settings?.userAgentString?.let {
                                    RequestHandler.checkIfLoginSuccess(
                                        base64Json,
                                        it
                                    )
                                }

                                if (t!!) {
                                    pref.saveData("cookies", base64Json)
                                    webView?.settings?.let {
                                        pref.saveData(
                                            "ua",
                                            it.userAgentString
                                        )
                                    }
                                    navController.navigate(Screen.HomeScreen.route) {
                                        popUpTo(Screen.HomeScreen.route) { inclusive = false }
                                    }
                                } else {
                                    showDialog = true
                                }
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
        Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context)
            },

            update = { view ->
                webView = view.apply {
                    settings.javaScriptEnabled = true
                    this.setWebChromeClient(WebChromeClient());
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            view?.setWebChromeClient(WebChromeClient());
//                            view?.evaluateJavascript("document.querySelector(\"#login_auto_login\").checked = \"true\"", null)
                        }

                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            isLoading.value = false
                            view.setWebChromeClient(WebChromeClient());
                            view.evaluateJavascript(
                                "document.querySelector(\"#login_auto_login\").checked = \"true\"",
                                null
                            )
                            view.evaluateJavascript(
                                "if (!window.eruda) {let parent = document.head || document.documentElement; let script = parent.appendChild(document.createElement('script')); script.src = 'https://cdn.jsdelivr.net/npm/eruda'; script.onload = () => eruda.init();}",
                                null
                            );
                            scope.launch {
                                val cookieManager = CookieManager.getInstance()

                                var cookies = cookieManager.getCookie("https://am-pass.net")
                                if (cookies != null) {
                                    if (cookies.contains("nullsid") || cookies.split(";").size >= 5) {
                                        val uri = HttpUrl.Builder()
                                            .scheme("https")
                                            .host("am-pass.net")
                                            .build();

                                        val uri2 = HttpUrl.Builder()
                                            .scheme("https")
                                            .host("www.piugame.com")
                                            .build();
                                        val uri3 = HttpUrl.Builder()
                                            .scheme("https")
                                            .host("api.am-pass.net")
                                            .build();
                                        val parsedCookies =
                                            cookies.split(";").map { Cookie.parse(uri, it) }
                                                .toMutableList()
                                        parsedCookies += cookies.split(";")
                                            .map { Cookie.parse(uri2, it) }
                                        cookies = cookieManager.getCookie("https://api.am-pass.net")
                                        parsedCookies += cookies.split(";")
                                            .map { Cookie.parse(uri3, it) }
                                        val base64Json = parsedCookies.toJson().toBase64()
                                        val t = webView?.settings?.userAgentString?.let {

                                            RequestHandler.checkIfLoginSuccess(
                                                base64Json,
                                                it
                                            )
                                        }

                                        if (t!!) {

                                            pref.saveData("cookies", base64Json)
                                            webView?.settings?.let {
                                                pref.saveData(
                                                    "ua",
                                                    it.userAgentString
                                                )
                                            }
                                            navController.navigate(Screen.HomeScreen.route) {
                                                popUpTo(Screen.HomeScreen.route) {
                                                    inclusive = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    loadUrl("https://am-pass.net/")
                    setBackgroundColor(backgroundColor.toArgb())
                    this.setWebChromeClient(WebChromeClient());
                }
            }
        )
        }
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

val json = Json { ignoreUnknownKeys = true }

fun List<Cookie?>.toJson(): String = json.encodeToString(map {
    it?.let { it1 ->
        CookieData(
            name = it1.name,
            value = it.value,
            domain = it.domain,
            path = it.path,
            secure = it.secure,
            httpOnly = it.httpOnly,
            expires = it.expiresAt
        )
    }
})

fun String.toBase64(): String = Base64.encodeToString(toByteArray(), Base64.DEFAULT)