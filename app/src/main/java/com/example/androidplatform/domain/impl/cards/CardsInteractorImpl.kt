package com.example.androidplatform.domain.impl.cards

import com.example.androidplatform.data.network.cards.ActivateCardRequest
import com.example.androidplatform.domain.RepositoryCards
import com.example.androidplatform.domain.api.CardsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow

class CardsInteractorImpl(private val cardsRepository: RepositoryCards): CardsInteractor {
    override suspend fun getCards(): Flow<SearchResultData<List<Card>>> {
        return cardsRepository.getCards()
    }

    override suspend fun getCardCvc(cardId: Int): Flow<SearchResultData<Int>> {
        return cardsRepository.getCardCvc(cardId)
    }

    override suspend fun lockCardById(cardId: Int): Flow<SearchResultData<Card>> {
        return cardsRepository.lockCardById(cardId)
    }

    override suspend fun unlockCardById(cardId: Int): Flow<SearchResultData<Card>> {
        return cardsRepository.unlockCardById(cardId)
    }

    override suspend fun activateCardById(cardId: Int, cardRequest: ActivateCardRequest): Flow<SearchResultData<Card>> {
        return cardsRepository.activateCardById(cardId, cardRequest)
    }
}
