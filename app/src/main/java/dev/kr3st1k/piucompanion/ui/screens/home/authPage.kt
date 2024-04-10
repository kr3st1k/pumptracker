package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.R
import dev.kr3st1k.piucompanion.core.helpers.Crypto
import dev.kr3st1k.piucompanion.core.modules.LoginManager
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.ui.components.MyAlertDialog
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginPage(viewModel: LoginViewModel, navController: NavController) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyAlertDialog(showDialog = viewModel.showFailedDialog.value, title = "Failed",
            content = "Try Again", onDismiss = {
                viewModel.showFailedDialog.value = false
            })
        if (viewModel.enterHomeScreen.value)
            navController.navigate(Screen.NewsPage.route) {
                popUpTo(Screen.LoginPage.route) {
                    inclusive = true
                }
                popUpTo(Screen.NewsPage.route) {
                    inclusive = true
                }
                popUpTo(Screen.AuthLoadingPage.route) {
                    inclusive = true
                }
            }
        OutlinedTextField(
            value = viewModel.username.value,
            onValueChange = { viewModel.username.value = it },
            singleLine = true,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                autoCorrect = false
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onDone = { this.defaultKeyboardAction(ImeAction.Done); viewModel.onLoginClicked() }
            ),
            trailingIcon = {
                val image = if (passwordVisible)
                    ImageVector.vectorResource(R.drawable.outline_visibility_24)
                else ImageVector.vectorResource(R.drawable.outline_visibility_off_24)

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.onLoginClicked() }) {
            Text("Login")
        }
    }
    if (viewModel.isLoading.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
}

class LoginViewModel : ViewModel() {
    val username = mutableStateOf(TextFieldValue())
    val password = mutableStateOf(TextFieldValue())
    val showFailedDialog = mutableStateOf(false)
    val enterHomeScreen = mutableStateOf(false)
    val isLoading = mutableStateOf(false)

    fun onLoginClicked() {
        if (username.value.text == "" || password.value.text == "") {
            showFailedDialog.value = true
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val r = NetworkRepositoryImpl.loginToAmPass(username.value.text, password.value.text)
            if (r) {
                Crypto.encryptData(username.value.text)
                    ?.let {
                        Crypto.encryptData(password.value.text)
                            ?.let { it1 -> LoginManager().saveLoginData(it, it1) }
                    }
                enterHomeScreen.value = true
            } else
                showFailedDialog.value = true
            isLoading.value = false
        }
    }
}
