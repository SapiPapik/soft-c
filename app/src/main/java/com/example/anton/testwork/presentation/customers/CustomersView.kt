package com.example.anton.testwork.presentation.customers

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.anton.testwork.entity.Customer

interface CustomersView: MvpView {
    fun showListCustomers(list: List<Customer>)
    fun showFormAddNew()
    fun <T> addNewItem(newCustomer: T)
    @StateStrategyType(SkipStrategy::class)
    fun dismissDialog()
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(msg: String)
    fun <T> deleteItem(customers: T)
    @StateStrategyType(SkipStrategy::class)
    fun showEditDialog(customer: Customer)
    fun <T> updateItem(customer: T)
}