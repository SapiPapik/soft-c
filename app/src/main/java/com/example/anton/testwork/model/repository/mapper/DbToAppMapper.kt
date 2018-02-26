package com.example.anton.testwork.model.repository.mapper

import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.model.db.CardDb
import com.example.anton.testwork.model.db.CardTypeDb
import com.example.anton.testwork.model.db.CustomerDb

fun CardDb.toCardApp() = Card(
        id = this.id,
        number = this.number,
        cardType = this.cardType.let(CardTypeDb::toCardTypeApp),
        customer = this.customer.let(CustomerDb::toCustomerApp)
)

fun CardTypeDb.toCardTypeApp() = CardType(
        id = this.id,
        name = this.name,
        discount = this.discount
)

fun CustomerDb.toCustomerApp() = Customer(
        id = this.id,
        fullName = this.fullName,
        phoneNumber = this.phoneNumber
)
