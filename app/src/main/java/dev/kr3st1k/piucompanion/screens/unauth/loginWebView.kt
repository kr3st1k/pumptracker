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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.MyAlertDialog
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
    var dialogTitle by remember { mutableStateOf("Вход Провален!") }
    var dialogContent by remember { mutableStateOf("Попробуй авторизоваться на сайте и потом нажать.") }


    Column {

        TopAppBar(title = { Text(text = "Как войдете - нажмите на кнопку") },

            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ), actions = {
                IconButton(onClick = {

                    val cookieManager = CookieManager.getInstance()

                    val cookies = cookieManager.getCookie("https://piugame.com")
                    if (cookies == null) {
                        dialogTitle = "Вход Провален!"
                        dialogContent = "Попробуй авторизоваться на сайте и потом нажать."
                        showDialog = true
                    } else {
                        scope.launch {

                            val t = webView?.settings?.userAgentString?.let {
                                RequestHandler.checkIfLoginSuccess(
                                    cookies,
                                    it
                                )
                            }
                            if (t == true) {
                                navController.navigate(Screen.MainAccountScreen.route)
                            } else {
                                showDialog = true
                            }

                            println(t)
                        }

                    }
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                }
            }
        )

        MyAlertDialog(
            showDialog = showDialog,
            title = dialogTitle,
            content = dialogContent,
            onDismiss = { showDialog = false })

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    // Настройка WebView
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl("about:blank")
                    loadUrl("https://piugame.com/login.php")

                }
            },

            update = { view ->
                view.loadUrl("https://piugame.com/login.php")
                webView = view
            }
        )
    }
}