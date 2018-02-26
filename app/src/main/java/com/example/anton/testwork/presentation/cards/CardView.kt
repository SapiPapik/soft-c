package com.example.anton.testwork.presentation.cards

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Customer

interface CardView: MvpView {
    fun showCardList(list: List<Card>)
    fun <T> addNewItem(newItemList: T)
    fun <T> deleteItem(itemList: T)
    fun <T> updateItem(itemList: T)
    @StateStrategyType(SkipStrategy::class)
    fun dismissDialog()
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(msg: String)
    @StateStrategyType(SkipStrategy::class)
    fun showCreateNewCardDialog(listCardTypes: List<CardType>, listCustomers: List<Customer>)
    @StateStrategyType(SkipStrategy::class)
    fun showEditCardDialog(card: Card, listCardTypes: List<CardType>, listCustomers: List<Customer>)
}