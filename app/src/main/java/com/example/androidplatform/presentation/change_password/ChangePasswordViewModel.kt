package com.example.androidplatform.presentation.change_password

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.api.ChangePasswordInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.change_password.models.ChangePasswordState
import com.example.androidplatform.presentation.restoration_password.RestorePasswordViewModel.Companion.DEFAULT_PASSWORD
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel(
    application: Application,
    private val changePasswordInteractor: ChangePasswordInteractor,
    private val authenticationInteractor: AuthenticationInteractor
) : AndroidViewModel(application) {
    private var login: String? = null

    private val _screenState = MutableLiveData<ChangePasswordState>()
    val screenState: LiveData<ChangePasswordState>
        get() = _screenState
    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String>
        get() = _showToastMessage

    private val _errorInputPassword1 = MutableLiveData<Boolean>()
    val errorInputPassword1: LiveData<Boolean>
        get() = _errorInputPassword1

    private val _errorInputPassword2 = MutableLiveData<Boolean>()
    val errorInputPassword2: LiveData<Boolean>
        get() = _errorInputPassword2

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    private fun changePasswordRequest(
        password: String,
        login: String? = null
    ) {
        _screenState.value = ChangePasswordState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            login?.let {
                val token = authenticationInteractor.authentication(AuthRequest(login, DEFAULT_PASSWORD))
                token.collect { data ->
                    withContext(Dispatchers.Main) {
                        when (data) {
                            is SearchResultData.Data -> _screenState.value = ChangePasswordState.ContentAuth(
                                data.value?.authenticationItem!!
                            )
                            is SearchResultData.ErrorServer -> {
                                _screenState.value = ChangePasswordState.Error(data.message)
                                _showToastMessage.postValue("Нет интернета")
                            }
                            is SearchResultData.NoInternet -> {
                                _screenState.value = ChangePasswordState.Error(data.message)
                                _showToastMessage.postValue("Нет интернета")
                            }
                            is SearchResultData.Empty -> {
                                _screenState.value = ChangePasswordState.Error(data.message)
                                _showToastMessage.postValue("Нет интернета")
                            }
                        }
                    }
                }
            }
            val result = changePasswordInteractor.changePassword(password)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            ChangePasswordState.Content

                        is SearchResultData.ErrorServer -> {
                            _screenState.value = ChangePasswordState.Error(data.message)
                            _showToastMessage.postValue(data.description)
                        }
                        is SearchResultData.NoInternet -> {
                            _screenState.value = ChangePasswordState.Error(data.message)
                            _showToastMessage.postValue(getApplication<Application>().resources.getString(data.message))
                        }
                        is SearchResultData.Empty -> {
                            _screenState.value = ChangePasswordState.Error(data.message)
                            _showToastMessage.postValue(getApplication<Application>().resources.getString(data.message))
                        }
                    }
                }
            }
        }
    }

    fun changePassword(password1: String?, password2: String?) {
        val fieldsValidPassword1 = validateInput(parseField(password1))
        val fieldsValidPassword2 = validateInput2(parseField(password2))

        if (fieldsValidPassword1 && fieldsValidPassword2) {
            if (password1 == password2) {
                login?.let {
                    changePasswordRequest(login = it, password = password1 ?: DEFAULT_PASSWORD)
                }
            } else {
                _errorInputPassword1.value = true
                _errorInputPassword2.value = true
            }
        }
    }

    private fun parseField(inputField: String?): String {
        return inputField?.trim() ?: ""
    }

    private fun validateInput(password: String): Boolean {
        var result = true
        if (password.matches(PASSWORD_REGEX.toRegex())) {
            _errorInputPassword1.value = true
            result = false
        }

        enableButton(result)

        return result
    }

    private fun validateInput2(password: String): Boolean {
        var result = true
        if (password.matches(PASSWORD_REGEX.toRegex())) {
            _errorInputPassword2.value = true
            result = false
        }

        enableButton(result)

        return result
    }

    private fun enableButton(enabled: Boolean) {
        _isButtonEnabled.value = enabled
    }

    fun setLogin(login: String?) {
        this.login = login
    }

    fun resetErrorInputPassword() {
        _errorInputPassword1.value = false
        _errorInputPassword2.value = false
    }

    fun changePassword1(password: String?) {
        val password = parseField(password)
        resetErrorInputPassword()

        _isButtonEnabled.value = password.isNotEmpty() && _errorInputPassword2.value != true
    }

    fun changePassword2(password: String?) {
        val password = parseField(password)
        resetErrorInputPassword()

        _isButtonEnabled.value = password.isNotEmpty() && _errorInputPassword1.value != true
    }

    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*)(?=.*[@$!%*?&])[A-Za-z@$!%*?&]{6,10}$"
    }
}

