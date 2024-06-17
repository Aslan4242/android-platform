package com.example.androidplatform.domain.impl.clients

import com.example.androidplatform.domain.RepositoryClients
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow

class ClientInteractorImpl(private val clientsRepository: RepositoryClients): ClientInteractor {
    override suspend fun getClients(): Flow<SearchResultData<Client>> {
        return clientsRepository.getClients()
    }
}
