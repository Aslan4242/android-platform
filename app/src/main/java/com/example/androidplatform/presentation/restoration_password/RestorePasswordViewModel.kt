package com.example.androidplatform.presentation.restoration_password

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.R
import com.example.androidplatform.domain.api.RestorePasswordInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.restoration_password.models.RestorePasswordState
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestorePasswordViewModel(
    application: Application,
    private val restorePasswordInteractor: RestorePasswordInteractor
) : AndroidViewModel(application) {

    private var countDownTimer: CountDownTimer? = null
    private val lastLogin = ""

    private val _screenState = MutableLiveData<RestorePasswordState>()
    val screenState: LiveData<RestorePasswordState>
        get() = _screenState
    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String>
        get() = _showToastMessage

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _errorInputLogin = MutableLiveData<Boolean>()
    val errorInputLogin: LiveData<Boolean>
        get() = _errorInputLogin

    private val _errorInputCode = MutableLiveData<Boolean>()
    val errorInputCode: LiveData<Boolean>
        get() = _errorInputCode

    private val _isSendCode = MutableLiveData<Boolean>()
    val isSendCode: LiveData<Boolean>
        get() = _isSendCode

    private val _isChangeCode = MutableLiveData<Boolean>()
    val isChangeCode: LiveData<Boolean>
        get() = _isChangeCode

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    fun restorePassword(
        login: String
    ) {
        _screenState.value = RestorePasswordState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = restorePasswordInteractor.restorePassword(
                login = login
            )
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            RestorePasswordState.Content

                        is SearchResultData.ErrorServer -> {
                            _screenState.value = RestorePasswordState.Error(data.message)
                            _showToastMessage.postValue(data.description)
                        }
                        is SearchResultData.NoInternet -> {
                            _screenState.value = RestorePasswordState.Error(data.message)
                            _showToastMessage.postValue(getApplication<Application>().resources.getString(data.message))
                        }
                        is SearchResultData.Empty -> {
                            _screenState.value = RestorePasswordState.Error(data.message)
                            _showToastMessage.postValue(getApplication<Application>().resources.getString(data.message))
                        }
                    }
                }
            }
        }
    }

    private fun sendCode(login: String?) {
        val login = parseField(login)
        val fieldsValid = validateLogin(login)
        if (fieldsValid) {
            _isSendCode.value = true
            _isChangeCode.value = false
            enableButton(false)
            startTimer()
        }
    }

    fun changeLogin(login: String?) {
        val login = parseField(login)
        val fieldsValid = login.isNotEmpty()
        if (fieldsValid || lastLogin != login) {
            _isChangeCode.value = true
        }
        enableButton(fieldsValid)
    }

    fun changeCode(code: String?) {
        enableButton(code?.isNotEmpty() == true)
    }

    fun restartTimer() {
        startTimer()
    }

    private fun restoreCode(login: String?, code: String?) {
        val login = parseField(login)
        val code = parseField(code)
        val fieldsValid = validateInput(login, code)
        if (fieldsValid) {
            if (code == DEFAULT_CODE) {
                restorePassword(login)
            } else {
                _showToastMessage.postValue(getApplication<Application>().resources.getString(R.string.send_code_error))
            }
        }
    }

    fun sendRestoreCode(login: String?, code: String?) {
        if (_isChangeCode?.value != true && _isSendCode?.value == true) {
            restoreCode(login, code)
        } else {
            sendCode(login)
        }
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_IN_SECOND
        val minutes = seconds / SECOND_IN_MINUTES
        val secondsLeft = seconds % SECOND_IN_MINUTES
        return String.format("%02d:%02d", minutes, secondsLeft)
    }

    private fun startTimer() {
        if (countDownTimer == null) {
            countDownTimer = object : CountDownTimer(
                SECONDS_TO_RESEND_CODE * MILLIS_IN_SECOND,
                MILLIS_IN_SECOND
            ) {
                override fun onTick(millis: Long) {
                    _formattedTime.postValue(formatTime(millis))
                }

                override fun onFinish() {
                    _formattedTime.postValue(getApplication<Application>().resources.getString(R.string.resend_code))
                }
            }
        }

        countDownTimer?.start()
    }

    private fun parseField(inputField: String?): String {
        return inputField?.trim() ?: ""
    }

    private fun validateInput(login: String, code: String): Boolean {
        var result = validateLogin(login)
        if (code.length < DEFAULT_CODE.length) {
            _errorInputCode.value = true
            result = false
        }

        enableButton(result)
        return result
    }

    private fun validateLogin(login: String): Boolean {
        var result = true
        if (login.isBlank() || !login.matches(LATIN_REGEX.toRegex())) {
            _errorInputLogin.value = true
            result = false
        }

        enableButton(result)
        return result
    }

    fun enableButton(enabled: Boolean) {
        _isButtonEnabled.value = enabled
    }

    fun resetErrorInputLogin() {
        _errorInputLogin.value = false
    }

    fun resetErrorInputCode() {
        _errorInputCode.value = false
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

    companion object {
        private const val DEFAULT_CODE = "1111"
        const val DEFAULT_PASSWORD = "111111"
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECOND_IN_MINUTES = 60L
        private const val SECONDS_TO_RESEND_CODE = 10L
        private const val LATIN_REGEX = "^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*\$"
    }
}

