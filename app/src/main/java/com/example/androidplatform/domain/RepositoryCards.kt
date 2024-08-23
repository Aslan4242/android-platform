package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow

interface RepositoryCards {
    suspend fun getCards(): Flow<SearchResultData<List<Card>>>
    suspend fun getCardCvc(cardId: Int): Flow<SearchResultData<Int>>
    suspend fun lockCardById(cardId: Int): Flow<SearchResultData<Card>>
    suspend fun unlockCardById(cardId: Int): Flow<SearchResultData<Card>>
}
