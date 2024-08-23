package com.example.androidplatform.domain.impl.update_client

import com.example.androidplatform.domain.RepositoryUpdateClient
import com.example.androidplatform.domain.api.UpdateClientInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow

class UpdateClientInteractorImpl(private val repositoryUpdateClient: RepositoryUpdateClient) :
    UpdateClientInteractor {
    override suspend fun updateClient(client: Client): Flow<SearchResultData<Void>> {
        return repositoryUpdateClient.updateClient(client)
    }
}
