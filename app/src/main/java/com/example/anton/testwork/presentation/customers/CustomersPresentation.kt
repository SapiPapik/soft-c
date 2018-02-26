package com.example.anton.testwork.presentation.customers

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.anton.testwork.model.interactor.CustomerInteractor
import com.example.anton.testwork.entity.Customer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CustomersPresentation : MvpPresenter<CustomersView>() {
    companion object {
        const val NEW_CUSTOMER_SUCCESSFULLY_CREATED = "Новая запись о клиенте успешно создана"
        const val CUSTOMER_SUCCESSFULLY_EDITED = "Запись о клиенте успешно отредактирована"
        const val CUSTOMER_SUCCESSFULLY_DELETED = "Запись о клиенте успешно удалена"
    }

    private val customers = ArrayList<Customer>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        CustomerInteractor.getCustomers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeBy(
                        onSuccess = (::onGettingCustomers),
                        onError = {
                            viewState.showMessage("FAIL")
                        }
                )
    }

    private fun onGettingCustomers(customers: List<Customer>) {
        this.customers.addAll(customers)
        viewState.showListCustomers(this.customers)
    }

    fun onClickAddNew() {
        viewState.showFormAddNew()
    }

    fun onClickCreateButton(fullName: String, phoneNumber: String) {
        if (fullName.isEmpty() || phoneNumber.isEmpty()) return
        val newCustomer = Customer(customers.lastIndex + 2L, fullName, phoneNumber)
        CustomerInteractor.addCustomer(newCustomer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { viewState.dismissDialog() }
                .subscribeBy(
                        onComplete = {
                            customers.add(newCustomer)
                            viewState.addNewItem(newCustomer)
                            viewState.showMessage(NEW_CUSTOMER_SUCCESSFULLY_CREATED)
                        }, onError = {
                    viewState.showMessage("FAIL")
                }
                )
    }

    fun onClickDeleteButton(customer: Customer) {
        CustomerInteractor.deleteCustomerById(customer.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            viewState.deleteItem(customer)
                            viewState.showMessage(CUSTOMER_SUCCESSFULLY_DELETED)
                        },
                        onError = {
                            viewState.showMessage("FAIL")
                        }
                )
    }

    fun onClickEditButton(customer: Customer) {
        viewState.showEditDialog(customer)
    }

    fun onClickEditCustomer(newCustomer: Customer) {
        CustomerInteractor.updateCustomer(newCustomer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(viewState::dismissDialog)
                .subscribeBy(
                        onComplete = { onUpdateCustomer(newCustomer) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onUpdateCustomer(customer: Customer) {
        val pos = customers.indexOf(customers.find { it.id == customer.id })
        customers[pos] = customer
        viewState.updateItem(customer)
        viewState.showMessage(CUSTOMER_SUCCESSFULLY_EDITED)
    }
}