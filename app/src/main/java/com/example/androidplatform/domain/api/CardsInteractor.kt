package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow

interface CardsInteractor {
    suspend fun getCards(): Flow<SearchResultData<List<Card>>>
}
