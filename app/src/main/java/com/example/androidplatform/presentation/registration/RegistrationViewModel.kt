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

    fun createClient(
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
                        is SearchResultData.Data -> _screenState.value = RegistrationState.Content("Client created successfully")

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

    private fun getDateTime(birthdate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return if (birthdate.isNotEmpty()) {
            LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay().format(formatter)
        } else {
            // если не указана дата рождения, то ставим 01.04.2001
            LocalDateTime.of(2001, 4, 1, 0, 0).format(formatter)
        }
    }

    companion object {
        const val NO_INTERNET = "Нет интернета"
    }
}

