package com.example.anton.testwork.model.db

import android.os.Parcelable
import io.requery.*

@Entity
interface CustomerDb : Parcelable, Persistable {
    @get:Key
    @get:Generated
    var id: Long

    var fullName: String

    var phoneNumber: String

    @get:OneToOne
    var card: CardDb
}