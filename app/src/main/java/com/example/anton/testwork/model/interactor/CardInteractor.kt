package com.example.anton.testwork.model.interactor

import com.example.anton.testwork.App
import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.model.db.CardDb
import com.example.anton.testwork.model.repository.CardDbRepository
import com.example.anton.testwork.model.repository.mapper.toCardApp
import com.example.anton.testwork.model.repository.mapper.toCardDb
import com.example.anton.testwork.model.repository.mapper.toCardTypeDb
import com.example.anton.testwork.model.repository.mapper.toCustomerDb
import io.reactivex.Completable
import io.reactivex.Observable

class CardInteractor {
    companion object {
        private val dbRepo by lazy { CardDbRepository(App.instance.db) }

        fun addCard(card: Card): Completable =
                dbRepo.add(card.toCardDb(card.customer?.toCustomerDb(), card.cardType.toCardTypeDb()))

        fun getCards(): Observable<Card> = dbRepo.getAll().map(CardDb::toCardApp)

        fun deleteCardById(id: Long): Completable = dbRepo.deleteById(id)

        fun updateCard(card: Card): Completable =
                dbRepo.updateById(card.toCardDb(card.customer?.toCustomerDb(), card.cardType.toCardTypeDb()))
    }
}