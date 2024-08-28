package com.example.androidplatform.presentation.account_refill.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.AccountInteractor
import com.example.androidplatform.domain.api.AccountsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.presentation.account_info.models.AccountInfoState
import com.example.androidplatform.presentation.account_transfer.models.AccountTransferScreenState
import com.example.androidplatform.presentation.account_transfer.models.AccountTransferState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountRefillViewModel(
    private val accountInteractor: AccountInteractor,
    private val accountsInteractor: AccountsInteractor,
) : ViewModel() {

    private val _accountScreenState = MutableLiveData<AccountInfoState>()
    fun accountScreenState(): LiveData<AccountInfoState> = _accountScreenState

    private val _screenState = MutableLiveData<AccountTransferState>()
    fun screenState(): LiveData<AccountTransferState> = _screenState

    private val modeLiveData = MutableLiveData<AccountTransferScreenState>()
    fun observeMode(): LiveData<AccountTransferScreenState> = modeLiveData

    var accounts: List<Account> = emptyList()

    private val _cardNum = MutableLiveData<String?>()
    val cardNum: LiveData<String?>
        get() = _cardNum

    private val _recipientAccount = MutableLiveData<String?>()
    val recipientAccount: LiveData<String?>
        get() = _recipientAccount

    private val _sum = MutableLiveData<String?>()
    val sum: LiveData<String?>
        get() = _sum

    private val _isButtonVisible = MutableLiveData<Boolean>()
    val isButtonVisible: LiveData<Boolean>
        get() = _isButtonVisible

    init {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = accountsInteractor.getAccounts().first()) {
                is SearchResultData.Data -> {
                    accounts = result.value!!
                }

                is SearchResultData.Empty -> {
                    _screenState.postValue(AccountTransferState.Error(result.message))
                }

                is SearchResultData.ErrorServer -> {
                    _screenState.postValue(AccountTransferState.Error(result.message))
                }

                is SearchResultData.NoInternet -> {
                    _screenState.postValue(AccountTransferState.Error(result.message))
                }
            }
        }
        setMode(AccountTransferScreenState.AccountTransferScreen)
    }

    fun onRecipientAccountChooseClick() {
        setMode(AccountTransferScreenState.BottomSheet(accounts))
    }

    fun setMode(mode: AccountTransferScreenState) {
        modeLiveData.value = mode
    }

    fun getAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = accountInteractor.getAccount(accountId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _accountScreenState.value =
                            AccountInfoState.Content(data.value!!)

                        is SearchResultData.Empty -> _accountScreenState.value =
                            AccountInfoState.Error(data.message)

                        is SearchResultData.ErrorServer -> _accountScreenState.value =
                            AccountInfoState.Error(data.message)

                        is SearchResultData.NoInternet -> _accountScreenState.value =
                            AccountInfoState.Error(data.message)
                    }
                }
            }
        }
    }

    fun addCardNum(cardNum: String?) {
        _cardNum.value = cardNum
        checkFieldsForEmptyValues(cardNum = cardNum)
    }

    fun addRecipientAccount(recipientAccount: String?) {
        _recipientAccount.value = recipientAccount
        checkFieldsForEmptyValues(recipientAccount = recipientAccount)
    }

    fun addSum(sum: String?) {
        _sum.value = sum
        checkFieldsForEmptyValues(sum = sum)
    }

    private fun checkFieldsForEmptyValues(
        cardNum: String? = _cardNum.value,
        recipientAccount: String? = _recipientAccount.value,
        sum: String? = _sum.value,
    ) {
        _isButtonVisible.value = cardNum?.length == 16 &&
                recipientAccount?.isNotEmpty() == true &&
                sum?.isNotEmpty() == true
    }
}
