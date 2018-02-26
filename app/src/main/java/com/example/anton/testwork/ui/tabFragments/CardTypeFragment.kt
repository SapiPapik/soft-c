package com.example.anton.testwork.ui.tabFragments

import android.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.EditText
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.anton.testwork.R
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.presentation.cardType.CardTypePresenter
import com.example.anton.testwork.presentation.cardType.CardTypeView
import com.example.anton.testwork.ui.adapter.RecyclerViewAdapter
import com.example.anton.testwork.ui.base.BaseTabFragment
import kotlinx.android.synthetic.main.list_tab_fragment.*

class CardTypeFragment : BaseTabFragment("Вид карты"), CardTypeView {

    @InjectPresenter
    lateinit var presenter: CardTypePresenter

    override lateinit var adapter: RecyclerViewAdapter<*>

    override fun onResume() {
        super.onResume()
        btnCreateNew.setOnClickListener {
            presenter.onClickAddNewButton()
        }
        rvListCustomers.layoutManager = LinearLayoutManager(activity)
        presenter.onResume()
    }

    override fun showCardTypeList(cardTypeList: List<CardType>) {
        adapter = RecyclerViewAdapter(ArrayList(cardTypeList))
        rvListCustomers.adapter = adapter
    }

    override fun <T> onClickDeleteButton(itemList: T) {
        presenter.onClickDeleteButton(itemList as CardType)
    }

    override fun <T> onClickEditButton(itemList: T) {
        presenter.onClickEditButton(itemList as CardType)
    }

    override fun showEditCardTypeDialog(cardType: CardType) {
        val dialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_card_type, null)

        val etCardName: EditText = dialogLayout.findViewById(R.id.etCardName)
        val etCardDiscount: EditText = dialogLayout.findViewById(R.id.etCardDiscount)

        createAndShowDialog(dialogLayout, "Редактировать запись", "Редактировать")
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            if (validationOfData(listOf(etCardName, etCardDiscount))) {
                val newCardType = cardType
                newCardType.name = etCardName.text.toString()
                newCardType.discount = etCardDiscount.text.toString().toInt()
                presenter.onClickEditCardType(newCardType)
            }
        }
    }

    override fun showCreateNewCardTypeDialog() {
        val dialogLayout =
                LayoutInflater.from(activity).inflate(R.layout.dialog_card_type, null)

        val etCardName: EditText = dialogLayout.findViewById(R.id.etCardName)
        val etCardDiscount: EditText = dialogLayout.findViewById(R.id.etCardDiscount)

        createAndShowDialog(dialogLayout, "Добавить запись", "Добавить")
        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            if (validationOfData(listOf(etCardName, etCardDiscount))) {
                presenter.onClickCreateButton(
                        etCardName.text.toString(), etCardDiscount.text.toString().toInt()
                )
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
}



