package com.example.anton.testwork.model.db

import android.os.Parcelable
import io.requery.*

@Entity
interface CardDb : Parcelable, Persistable {
    @get:Key
    @get:Generated
    var id: Long

    var number: String

    @get:OneToOne
    @get:ForeignKey
    var cardType: CardTypeDb

    @get:OneToOne
    @get:Nullable
    @get:ForeignKey
    var customer: CustomerDb
}