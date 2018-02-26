package com.example.anton.testwork.entity

data class Card(
        var id: Long,
        var number: String,
        var cardType: CardType,
        var customer: Customer? = null
)