package com.example.androidplatform.domain.impl.cards

import com.example.androidplatform.domain.RepositoryCards
import com.example.androidplatform.domain.api.CardsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow

class CardsInteractorImpl(private val cardsRepository: RepositoryCards): CardsInteractor {
    override suspend fun getCards(): Flow<SearchResultData<List<Card>>> {
        return cardsRepository.getCards()
    }
}
