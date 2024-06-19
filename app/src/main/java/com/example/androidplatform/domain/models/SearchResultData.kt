package com.example.androidplatform.domain.models

import androidx.annotation.StringRes

sealed interface SearchResultData<T> {
    data class Data<T>(val value: T?) : SearchResultData<T>
    data class ErrorServer<T>(@StringRes val message: Int, val description: String) : SearchResultData<T>
    data class NoInternet<T>(@StringRes val message: Int) : SearchResultData<T>
    data class Empty<T>(@StringRes val message: Int) : SearchResultData<T>
}
