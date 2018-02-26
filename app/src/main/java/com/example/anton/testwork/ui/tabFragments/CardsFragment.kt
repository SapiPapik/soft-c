package com.example.anton.testwork.ui.tabFragments

import android.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.anton.testwork.R
import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.presentation.cards.CardView
import com.example.anton.testwork.presentation.cards.CardsPresenter
import com.example.anton.testwork.ui.adapter.RecyclerViewAdapter
import com.example.anton.testwork.ui.base.BaseTabFragment
import kotlinx.android.synthetic.main.list_tab_fragment.*

class CardsFragment : BaseTabFragment("Карты"), CardView {
    @InjectPresenter
    lateinit var presenter: CardsPresenter

    override fun onResume() {
        super.onResume()
        btnCreateNew.setOnClickListener {
            presenter.onClickAddNewCard()
        }
        rvListCustomers.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerViewAdapter(ArrayList<Card>())
        rvListCustomers.adapter = adapter
    }

    override lateinit var adapter: RecyclerViewAdapter<*>

    override fun <T> onClickDeleteButton(itemList: T) {
        presenter.onClickDeleteButton(itemList as Card)
    }

    override fun <T> onClickEditButton(itemList: T) {
        presenter.onClickEditButton(itemList as Card)
    }

    override fun showCardList(list: List<Card>) {
        addListItems(list)
    }

    override fun showCreateNewCardDialog(
            listCardTypes: List<CardType>,
            listCustomers: List<Customer>
    ) {
        val newCustomerDialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_card, null)

        val etCardNumber: EditText = newCustomerDialogLayout.findViewById(R.id.etCardNumber)
        val spinCardTypes: Spinner = newCustomerDialogLayout.findViewById(R.id.spinCardTypes)
        val spinCustomers: Spinner = newCustomerDialogLayout.findViewById(R.id.spinCustomers)

        spinCardTypes.adapter = initSpinnerAdapter(listCardTypes)
        spinCustomers.adapter = initSpinnerAdapter(listCustomers)

        createAndShowDialog(newCustomerDialogLayout, "Добавить запись", "Добавить")
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            presenter.onClickCreateButton(
                    etCardNumber.text.toString(),
                    listCardTypes[spinCardTypes.selectedItemPosition],
                    listCustomers[spinCustomers.selectedItemPosition]
            )
        }
    }

    override fun showEditCardDialog(
            card: Card,
            listCardTypes: List<CardType>,
            listCustomers: List<Customer>
    ) {
        val newCustomerDialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_card, null)

        val etCardNumber: EditText = newCustomerDialogLayout.findViewById(R.id.etCardNumber)
        val spinCardTypes: Spinner = newCustomerDialogLayout.findViewById(R.id.spinCardTypes)
        val spinCustomers: Spinner = newCustomerDialogLayout.findViewById(R.id.spinCustomers)

        spinCardTypes.adapter = initSpinnerAdapter(listCardTypes)
        spinCustomers.adapter = initSpinnerAdapter(listCustomers)

        etCardNumber.setText(card.number)
        spinCardTypes.setSelection(listCardTypes.indexOf(card.cardType))
        spinCustomers.setSelection(listCustomers.indexOf(card.customer))

        createAndShowDialog(newCustomerDialogLayout, "Редактировать запись", "Редактировать")
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val newCard = card
            newCard.number = etCardNumber.text.toString()
            newCard.cardType = listCardTypes[spinCardTypes.selectedItemPosition]
            newCard.customer = listCustomers[spinCustomers.selectedItemPosition]
            presenter.onClickEditCard(newCard)
        }
    }

    private fun <T> initSpinnerAdapter(list: List<T>): ArrayAdapter<String> {
        val listOfItemsSpinner = ArrayList<String>()
        when (list.first()) {
            is CardType -> list.forEach {
                listOfItemsSpinner.add((it as CardType).name + " " + (it as CardType).discount)
            }
            is Customer -> list.forEach {
                listOfItemsSpinner.add((it as Customer).fullName + " " + (it as Customer).phoneNumber)
            }
        }
        return ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listOfItemsSpinner)
    }
}

