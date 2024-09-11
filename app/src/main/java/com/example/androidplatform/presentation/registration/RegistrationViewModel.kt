package com.example.androidplatform.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.RegistrationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.registration.models.RegistrationState
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegistrationViewModel(
    private val registrationInteractor: RegistrationInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<RegistrationState>()
    fun screenState(): LiveData<RegistrationState> = _screenState
    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

    private val _login = MutableLiveData<String?>()
    val login: LiveData<String?>
        get() = _login

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?>
        get() = _email

    private val _birthdate = MutableLiveData<String?>()
    val birthdate: LiveData<String?>
        get() = _birthdate

    private val _password = MutableLiveData<String?>()
    val password: LiveData<String?>
        get() = _password

    private val _passwordRepeat = MutableLiveData<String?>()
    val passwordRepeat: LiveData<String?>
        get() = _passwordRepeat

    private val _errorInputPassword1 = MutableLiveData<Boolean>()
    val errorInputPassword1: LiveData<Boolean>
        get() = _errorInputPassword1

    private val _errorInputPassword2 = MutableLiveData<Boolean>()
    val errorInputPassword2: LiveData<Boolean>
        get() = _errorInputPassword2

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    fun registration(
        login: String,
        password: String,
        repeatPassword: String,
        phoneNumber: String,
        email: String,
        name: String,
        surname: String,
        patronymic: String,
        birthdate: String,
        address: String,
        sex: String
    ) {
        val fieldsValidPassword1 = validateInput(parseField(password))
        val fieldsValidPassword2 = validateInput2(parseField(repeatPassword))

        if (fieldsValidPassword1 && fieldsValidPassword2) {
            if (password == repeatPassword) {
                createClient(
                    login = login,
                    password = password,
                    phoneNumber = phoneNumber,
                    email = email,
                    name = name,
                    surname = surname,
                    patronymic = patronymic,
                    birthdate = birthdate,
                    address = address,
                    sex = sex
                )
            } else {
                _errorInputPassword1.value = true
                _errorInputPassword2.value = true
            }
        }
    }

    private fun createClient(
        login: String,
        password: String,
        phoneNumber: String,
        email: String,
        name: String,
        surname: String,
        patronymic: String,
        birthdate: String,
        address: String,
        sex: String
    ) {
        _screenState.value = RegistrationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = registrationInteractor.createClient(
                login = login,
                password = password,
                phoneNumber = phoneNumber,
                email = email,
                name = name,
                surname = surname,
                patronymic = patronymic,
                birthdate = getDateTime(birthdate),
                address = address,
                sex = sex
            )
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            RegistrationState.Content("Client created successfully")

                        is SearchResultData.ErrorServer -> {
                            _screenState.value = RegistrationState.Error(data.message)
                            _showToastMessage.postValue(data.description)
                        }

                        is SearchResultData.NoInternet -> {
                            _screenState.value = RegistrationState.Error(data.message)
                            _showToastMessage.postValue(NO_INTERNET)
                        }

                        is SearchResultData.Empty -> {
                            _screenState.value = RegistrationState.Error(data.message)
                            _showToastMessage.postValue(NO_INTERNET)
                        }
                    }
                }
            }
        }
    }

    fun addLogin(login: String?) {
        _login.value = login
        checkFieldsForEmptyValues(login = login)
    }

    fun addEmail(email: String?) {
        _email.value = email
        checkFieldsForEmptyValues(email = email)
    }

    fun addBirthdate(birthdate: String?) {
        _birthdate.value = birthdate
        checkFieldsForEmptyValues(birthdate = birthdate)
    }

    fun addPassword1(password: String?) {
        _password.value = password
        val parsedPassword = parseField(password)
        resetErrorInputPassword()

        checkFieldsForEmptyValues(password = parsedPassword)
    }

    fun addPassword2(password: String?) {
        _passwordRepeat.value = password
        val parsedPassword = parseField(password)
        resetErrorInputPassword()

        checkFieldsForEmptyValues(passwordRepeat = parsedPassword)
    }

    private fun resetErrorInputPassword() {
        _errorInputPassword1.value = false
        _errorInputPassword2.value = false
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

        return result
    }

    private fun validateInput2(password: String): Boolean {
        var result = true
        if (password.matches(PASSWORD_REGEX.toRegex())) {
            _errorInputPassword2.value = true
            result = false
        }

        return result
    }

    private fun checkFieldsForEmptyValues(
        login: String? = _login.value,
        email: String? = _email.value,
        birthdate: String? = _birthdate.value,
        password: String? = _password.value,
        passwordRepeat: String? = _passwordRepeat.value
    ) {
        _isButtonEnabled.value = login?.isNotEmpty() == true &&
                email?.isNotEmpty() == true &&
                birthdate?.isNotEmpty() == true &&
                (password?.length!! in MIN_LENGTH..MAX_LENGTH) &&
                (passwordRepeat?.length!! in MIN_LENGTH..MAX_LENGTH)
    }

    private fun getDateTime(birthdate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay()
            .format(formatter)

    }

    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*)(?=.*[@$!%*?&])[A-Za-z@$!%*?&]{6,10}$"
        const val NO_INTERNET = "Нет интернета"
        const val MIN_LENGTH = 8
        const val MAX_LENGTH = 29
    }
}

