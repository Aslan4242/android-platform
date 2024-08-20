package com.example.androidplatform.presentation.update_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.api.UpdateClientInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.update_user.model.UpdateState
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateUserViewModel(
    private val updateUserInteractor: UpdateClientInteractor,
    private val clientInteractor: ClientInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenStateClients>()
    fun screenState(): LiveData<ScreenStateClients> = _screenState

    private val _updateUserState = MutableLiveData<UpdateState>()
    val updateUserState: LiveData<UpdateState> = _updateUserState

    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

    private lateinit var oldClient: Client

    private val _isButtonEnabled = MutableLiveData(false)
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    fun getClients() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = clientInteractor.getClients()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> {
                            oldClient = data.value!!
                            _screenState.value = ScreenStateClients.Content(data.value)
                        }

                        is SearchResultData.ErrorServer ->
                            _screenState.value = ScreenStateClients.Error(data.message)

                        is SearchResultData.NoInternet ->
                            _screenState.value = ScreenStateClients.Error(data.message)

                        is SearchResultData.Empty ->
                            _screenState.value = ScreenStateClients.Error(data.message)
                    }
                }
            }
        }
    }

    fun updateUser(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updateUserInteractor.updateClient(client)

            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data ->
                            _updateUserState.value = UpdateState.Content(USER_UPDATED)

                        is SearchResultData.ErrorServer ->
                            _updateUserState.value = UpdateState.Error(data.message)

                        is SearchResultData.NoInternet ->
                            _updateUserState.value = UpdateState.Error(data.message)

                        is SearchResultData.Empty ->
                            _updateUserState.value = UpdateState.Error(data.message)
                    }
                }
            }
        }
    }

    fun checkEnteredText(tempClient: Client) {
        _isButtonEnabled.value = tempClient != oldClient
                && tempClient.login.isNotEmpty() == true
                && tempClient.email.isNotEmpty() == true
    }

    fun getDateTime(birthdate: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val tempDateStr = birthdate.split('T')
        return LocalDate.parse(
            tempDateStr[0],
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
            .atStartOfDay()
            .format(formatter)
    }

    fun getServerDateTime(birthdate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .atStartOfDay()
            .format(formatter)
    }

    companion object {
        const val USER_UPDATED = "Пользователь успешно обновлён"
    }
}
