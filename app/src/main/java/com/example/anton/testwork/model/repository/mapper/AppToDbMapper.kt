package com.example.anton.testwork.model.repository.mapper

import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.model.db.*

fun Card.toCardDb(customerDb: CustomerDb? = null, cardTypeDb: CardTypeDb) = CardDbEntity().also { cardDb ->
    cardDb.id = id
    cardDb.number = number
    cardDb.customer = customerDb
    cardDb.cardType = cardTypeDb
}

fun CardType.toCardTypeDb() = CardTypeDbEntity().also { cardTypeDb ->
    cardTypeDb.id = id
    cardTypeDb.discount = discount
    cardTypeDb.name = name
}

fun Customer.toCustomerDb() = CustomerDbEntity().also { customerDb ->
    customerDb.id = id
    customerDb.fullName = fullName
    customerDb.phoneNumber = phoneNumber
}
