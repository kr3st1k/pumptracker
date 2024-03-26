package dev.kr3st1k.piucompanion.screens.home.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.User
import kotlinx.coroutines.launch

class UserViewModel(
    private val pref: PreferencesManager,
) : ViewModel() {

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
            _checkLogin.value = RequestHandler.checkIfLoginSuccess(
                pref.getData("cookies", ""),
                pref.getData("ua", "")
            )
            _checkingLogin.value = false
            if (_checkLogin.value == true) {
                getUserInfo()
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _user.value =
                RequestHandler.getUserInfo(pref.getData("cookies", ""), pref.getData("ua", ""))
        }
    }

    fun logout(pref: PreferencesManager) {
        pref.removeLoginData()
    }
}

class UserViewModelFactory(private val pref: PreferencesManager) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

