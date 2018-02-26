package com.example.anton.testwork.model.repository

import android.util.Log
import com.example.anton.testwork.model.db.CustomerDb
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toCompletable
import io.requery.Persistable
import io.requery.kotlin.eq
import io.requery.reactivex.KotlinReactiveEntityStore

class CustomerDbRepository(val db: KotlinReactiveEntityStore<Persistable>) {
    companion object {
        private const val TAG = "CDBR"
    }

    fun add(customerDb: CustomerDb): Completable = Completable.create { subs ->
        db.insert(customerDb)
                .doOnSubscribe { Log.d(TAG, "Start inserting $customerDb") }
                .subscribeBy(
                        onSuccess = { item ->
                            Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $customerDb")
                            e.printStackTrace()
                            subs.onError(e)
                        }
                )
    }

    fun getAll(): Observable<CustomerDb> =
            (db.select(CustomerDb::class)).get().observable() ?: io.reactivex.Observable.empty()

    fun deleteById(id: Long): Completable =
            (db.delete(CustomerDb::class) where (CustomerDb::id eq id)).get().toCompletable()

    fun updateById(customerDb: CustomerDb): Completable = Completable.create { subs ->
        (db.update(customerDb)
                .doOnSubscribe { Log.d(TAG, "Start batch inserting") }
                .subscribeBy(
                        onSuccess = { item ->
                            Log.d(TAG, "Inserted $item")
                            subs.onComplete()
                        },
                        onError = { e ->
                            Log.d(TAG, "Error happened while inserting $customerDb")
                            e.printStackTrace()
                            subs.onError(e)
                        }
                ))
    }
}