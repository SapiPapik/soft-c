package com.example.anton.testwork.presentation.cardType

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.anton.testwork.entity.CardType

interface CardTypeView: MvpView {
    fun showCardTypeList(cardTypeList: List<CardType>)
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(msg: String)
    fun <T> addNewItem(newItemList: T)
    fun <T> deleteItem(itemList: T)
    fun <T> updateItem(itemList: T)
    @StateStrategyType(SkipStrategy::class)
    fun dismissDialog()
    @StateStrategyType(SkipStrategy::class)
    fun showEditCardTypeDialog(cardType: CardType)
    @StateStrategyType(SkipStrategy::class)
    fun showCreateNewCardTypeDialog()
}