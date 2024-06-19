package com.example.androidplatform.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.RegistrationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.registration.models.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RegistrationViewModel(
    private val registrationInteractor: RegistrationInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<RegistrationState>()
    fun screenState(): LiveData<RegistrationState> = _screenState

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

                        is SearchResultData.ErrorServer -> _screenState.value =
                            RegistrationState.Error(data.message)

                        is SearchResultData.NoInternet -> _screenState.value =
                            RegistrationState.Error(data.message)

                        is SearchResultData.Empty -> _screenState.value =
                            RegistrationState.Error(data.message)
                    }
                }
            }
        }
    }

    private fun getDateTime(birthdate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return birthdate.ifEmpty { ZonedDateTime.now().format(formatter)}
    }
}

