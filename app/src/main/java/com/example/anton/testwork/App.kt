package com.example.anton.testwork

import android.app.Application
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode
import com.example.anton.testwork.model.db.Models

class App: Application() {
    companion object {
        lateinit var instance: App
    }
    val db: KotlinReactiveEntityStore<Persistable> by lazy {
        val source = DatabaseSource(this, Models.DEFAULT, 2)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(source.configuration))
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}