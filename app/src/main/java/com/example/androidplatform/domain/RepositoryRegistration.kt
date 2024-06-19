package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

interface RepositoryRegistration {
    suspend fun createClient(
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
    ): Flow<SearchResultData<Void>>
}