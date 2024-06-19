package com.example.androidplatform.domain.impl.registration

import com.example.androidplatform.domain.RepositoryRegistration
import com.example.androidplatform.domain.api.RegistrationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

class RegistrationInteractorImpl(private val repositoryRegistration: RepositoryRegistration): RegistrationInteractor {
    override suspend fun createClient(
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
    ): Flow<SearchResultData<Void>> {
        return repositoryRegistration.createClient(
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
    }

}
