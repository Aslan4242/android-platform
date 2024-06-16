package com.example.androidplatform.domain.impl.clients

import com.example.androidplatform.domain.RepositoryClients
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Clients
import kotlinx.coroutines.flow.Flow

class ClientInteractorImpl(private val clientsRepository: RepositoryClients): ClientInteractor {
    override suspend fun getClients(token: String): Flow<SearchResultData<Clients>> {
        return clientsRepository.getClients(token)
    }
}
