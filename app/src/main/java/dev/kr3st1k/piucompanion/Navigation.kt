package dev.kr3st1k.piucompanion

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.state.PIUViewModel
import kotlinx.coroutines.launch

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route)
    {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Screen.LoginWebViewScreen.route) {
            LoginWebViewScreen(navController = navController)
        }
        composable(route = Screen.MainAccountScreen.route) {
            MainAccountScreen(navController = navController)
        }
    }
}


@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo of piu",
        )

        Button(
            onClick = {
                navController.navigate(Screen.LoginWebViewScreen.route)
            },
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {
            Text(text = "Войти в аккаунт")
        }

    }
}

@Composable
fun MyAlertDialog(
    showDialog: Boolean,
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = {
                Text(text = content)
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    // Действие при нажатии на кнопку "Подтвердить"
                }) {
                    Text(text = "Ща зайду, пажжи")
                }
            }
        )
    }
}

@Composable
fun MainAccountScreen(navController: NavController, viewModel: PIUViewModel = viewModel()) {


}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginWebViewScreen(navController: NavController, viewModel: PIUViewModel = viewModel()) {

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
                    if (cookies == null)
                    {
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

