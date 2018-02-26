package com.example.anton.testwork.ui.tabFragments

import android.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.EditText
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.anton.testwork.R
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.presentation.customers.CustomersPresentation
import com.example.anton.testwork.presentation.customers.CustomersView
import com.example.anton.testwork.ui.base.BaseTabFragment
import kotlinx.android.synthetic.main.list_tab_fragment.*
import com.example.anton.testwork.ui.adapter.RecyclerViewAdapter

class CustomersFragment : BaseTabFragment("Клиенты"), CustomersView {

    @InjectPresenter
    lateinit var presenter: CustomersPresentation

    override lateinit var adapter: RecyclerViewAdapter<*>

    override fun onResume() {
        super.onResume()
        btnCreateNew.setOnClickListener {
            presenter.onClickAddNew()
        }
        rvListCustomers.layoutManager = LinearLayoutManager(activity)
    }

    override fun showListCustomers(list: List<Customer>) {
        adapter = RecyclerViewAdapter(ArrayList(list))
        rvListCustomers.adapter = adapter
    }

    override fun showFormAddNew() {
        val newCustomerDialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_customers, null)

        val etFullName: EditText = newCustomerDialogLayout.findViewById(R.id.etFullName)
        val etPhoneNumber: EditText = newCustomerDialogLayout.findViewById(R.id.etPhoneNumber)

        createAndShowDialog(newCustomerDialogLayout, "Добавить запись", "Добавить")
        resetErrorFromEditText(etFullName)
        resetErrorFromEditText(etPhoneNumber)
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            if (validationOfData(listOf(etFullName, etPhoneNumber))) {
                presenter.onClickCreateButton(
                        etFullName.text.toString(), etPhoneNumber.text.toString()
                )
            }
        }
    }

    override fun showEditDialog(customer: Customer) {
        val newCustomerDialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_customers, null)

        val etFullName: EditText = newCustomerDialogLayout.findViewById(R.id.etFullName)
        val etPhoneNumber: EditText = newCustomerDialogLayout.findViewById(R.id.etPhoneNumber)

        etFullName.setText(customer.fullName)
        etPhoneNumber.setText(customer.phoneNumber)
        createAndShowDialog(newCustomerDialogLayout, "Редактировать запись", "Редактировать")
        resetErrorFromEditText(etFullName)
        resetErrorFromEditText(etPhoneNumber)
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            if (validationOfData(listOf(etFullName, etPhoneNumber))) {
                val newCustomer = customer
                newCustomer.fullName = etFullName.text.toString()
                newCustomer.phoneNumber = etPhoneNumber.text.toString()
                presenter.onClickEditCustomer(newCustomer)
            }
        }
    }

    private fun validationOfData(listOfEditText: List<EditText>): Boolean {
        for (item in listOfEditText) {
            if (item.text.isEmpty()) run {
                item.error = "Обязательное поле"
                return false
            }
        }
        return true
    }

    override fun <T> onClickEditButton(itemList: T) {
        presenter.onClickEditButton(itemList as Customer)
    }

    override fun <T> onClickDeleteButton(itemList: T) {
        presenter.onClickDeleteButton(itemList as Customer)
    }
}