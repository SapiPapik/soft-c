package com.example.anton.testwork.model.interactor

import com.example.anton.testwork.App
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.model.db.CustomerDb
import com.example.anton.testwork.model.repository.CustomerDbRepository
import com.example.anton.testwork.model.repository.mapper.toCustomerApp
import com.example.anton.testwork.model.repository.mapper.toCustomerDb
import io.reactivex.Completable
import io.reactivex.Observable

class CustomerInteractor {
    companion object {
        private val dbRepo by lazy { CustomerDbRepository(App.instance.db) }

        fun addCustomer(customer: Customer): Completable =
                dbRepo.add(customer.toCustomerDb())

        fun getCustomers(): Observable<Customer> = dbRepo.getAll().map(CustomerDb::toCustomerApp)

        fun deleteCustomerById(id: Long): Completable = dbRepo.deleteById(id)

        fun updateCustomer(customer: Customer): Completable =
                dbRepo.updateById(customer.toCustomerDb())
    }
}