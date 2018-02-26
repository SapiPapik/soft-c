package com.example.anton.testwork.model.interactor

import com.example.anton.testwork.App
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.model.db.CardTypeDb
import com.example.anton.testwork.model.repository.CardTypeDbRepository
import com.example.anton.testwork.model.repository.mapper.toCardTypeApp
import com.example.anton.testwork.model.repository.mapper.toCardTypeDb
import io.reactivex.Completable
import io.reactivex.Observable

class CardTypeInteractor {
    companion object {
        private val dbRepo by lazy { CardTypeDbRepository(App.instance.db) }

        fun addCardType(cardType: CardType): Completable =
                dbRepo.add(cardType.toCardTypeDb())

        fun getCardTypes(): Observable<CardType> = dbRepo.getAll().map(CardTypeDb::toCardTypeApp)

        fun deleteCardTypeById(id: Long): Completable = dbRepo.deleteById(id)

        fun updateCardType(cardType: CardType): Completable =
                dbRepo.updateById(cardType.toCardTypeDb())
    }
}