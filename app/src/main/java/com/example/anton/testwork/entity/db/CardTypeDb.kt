package com.example.anton.testwork.model.db

import android.os.Parcelable
import io.requery.*

@Entity
interface CardTypeDb : Parcelable, Persistable {
    @get:Key
    @get:Generated
    var id: Long

    var name: String

    var discount: Int

    @get:OneToOne
    var card: CardDb
}