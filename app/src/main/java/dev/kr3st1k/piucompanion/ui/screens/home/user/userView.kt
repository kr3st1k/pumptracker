package dev.kr3st1k.piucompanion.ui.screens.home.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        checkLoginAndUser()
    }

    //UNUSED
    fun login() {
        viewModelScope.launch {
            RequestHandler.loginToAmPass("no@no.no", "nono", true)
        }
    }

    private fun checkLoginAndUser() {
        viewModelScope.launch {
            _checkLogin.value = RequestHandler.checkIfLoginSuccess()
            _checkingLogin.value = false
            if (_checkLogin.value == true) {
                getUserInfo()
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _user.value =
                RequestHandler.getUserInfo()
        }
    }

    fun logout(pref: PreferencesManager) {
        pref.removeLoginData()
    }
}

