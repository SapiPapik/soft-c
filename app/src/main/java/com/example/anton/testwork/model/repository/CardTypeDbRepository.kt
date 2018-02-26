package com.example.anton.testwork.model.repository

import android.util.Log
import com.example.anton.testwork.model.db.CardTypeDb
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toCompletable
import io.requery.Persistable
import io.requery.kotlin.eq
import io.requery.reactivex.KotlinReactiveEntityStore

class CardTypeDbRepository(val db: KotlinReactiveEntityStore<Persistable>) {
    companion object {
        private const val TAG = "CTDBR"
    }

    fun add(cardTypeDb: CardTypeDb): Completable = Completable.create { subs ->
        db.insert(cardTypeDb)
                .doOnSubscribe { Log.d(TAG, "Start inserting $cardTypeDb") }
                .subscribeBy(
                        onSuccess = { item ->
                            Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $cardTypeDb")
                            e.printStackTrace()
                            subs.onError(e)
                        }
                )
    }

    fun getAll(): Observable<CardTypeDb> =
            (db.select(CardTypeDb::class)).get().observable() ?: io.reactivex.Observable.empty()

    fun deleteById(id: Long): Completable =
            (db.delete(CardTypeDb::class) where (CardTypeDb::id eq id)).get().toCompletable()

    fun updateById(cardTypeDb: CardTypeDb): Completable = Completable.create { subs ->
        (db.update(cardTypeDb)
                .doOnSubscribe { Log.d(TAG, "Start batch inserting") }
                .subscribeBy(
                        onSuccess = { item ->
                            Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $cardTypeDb")
                            e.printStackTrace()
                            subs.onError(e)
                        }
                ))
    }
}