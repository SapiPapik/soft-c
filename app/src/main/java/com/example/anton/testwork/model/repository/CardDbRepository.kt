package com.example.anton.testwork.model.repository

import android.util.Log
import com.example.anton.testwork.model.db.CardDb
import io.reactivex.Completable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toCompletable
import io.requery.kotlin.eq

class CardDbRepository(val db: KotlinReactiveEntityStore<Persistable>) {
    companion object {
        private const val TAG = "[CDBR]"
    }

    fun add(cardDb: CardDb): Completable = Completable.create { subs ->
        db.insert(cardDb)
                .doOnSubscribe { Log.d(TAG, "Start inserting $cardDb") }
                .subscribeBy(
                        onSuccess = {
                            item -> Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $cardDb")
                            e.printStackTrace()
                            subs.onError(e)
                        }
                )
    }

    fun getAll(): Observable<CardDb> =
            (db.select(CardDb::class)).get().observable() ?: io.reactivex.Observable.empty()

    fun deleteById(id: Long): Completable =
            (db.delete(CardDb::class) where (CardDb::id eq id)).get().toCompletable()

    fun updateById(cardDb: CardDb): Completable = Completable.create { subs ->
        (db.update(cardDb)
                .doOnSubscribe { Log.d(TAG, "Start batch inserting") }
                .subscribeBy(
                        onSuccess = { item ->
                            Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $cardDb")
                            e.printStackTrace()
                            subs.onError(Exception())
                        }
                ))
    }
}