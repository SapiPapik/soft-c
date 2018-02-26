package com.example.anton.testwork.presentation.cardType

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.anton.testwork.model.interactor.CardTypeInteractor
import com.example.anton.testwork.entity.CardType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CardTypePresenter : MvpPresenter<CardTypeView>() {
    companion object {
        const val CARD_TYPE_SUCCESSFULLY_DELETED = "Запись о типе карты успешно удалена"
        const val CARD_TYPE_SUCCESSFULLY_EDITED = "Запись о типе карты успешно отредактирована"
        const val CARD_TYPE_SUCCESSFULLY_CREATED = "Новая запись о типе карты успешно создана"
    }

    val cardTypeList = ArrayList<CardType>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        CardTypeInteractor.getCardTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeBy(
                        onSuccess = (::onSuccessGetCardTypes),
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessGetCardTypes(cardTypes: List<CardType>) {
        cardTypeList.addAll(cardTypes)
        viewState.showCardTypeList(cardTypeList)
    }

    fun onResume() {
        viewState.showCardTypeList(cardTypeList)
    }

    fun onClickAddNewButton() {
        viewState.showCreateNewCardTypeDialog()
    }

    fun onClickDeleteButton(cardType: CardType) {
        CardTypeInteractor.deleteCardTypeById(cardType.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { onSuccessDelete(cardType) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessDelete(cardType: CardType) {
        cardTypeList.remove(cardType)
        viewState.deleteItem(cardType)
        viewState.showMessage(CARD_TYPE_SUCCESSFULLY_DELETED)
    }

    fun onClickEditButton(cardType: CardType) {
        viewState.showEditCardTypeDialog(cardType)
    }

    fun onClickEditCardType(newCardType: CardType) {
        CardTypeInteractor.updateCardType(newCardType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(viewState::dismissDialog)
                .subscribeBy(
                        onComplete = { onSuccessEdit(newCardType) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessEdit(cardType: CardType) {
        val pos = cardTypeList.indexOf(cardTypeList.find { it.id == cardType.id })
        cardTypeList[pos] = cardType
        viewState.updateItem(cardType)
        viewState.showMessage(CARD_TYPE_SUCCESSFULLY_EDITED)
    }

    fun onClickCreateButton(cardName: String, cardDiscount: Int) {
        if (cardName.isEmpty() || cardDiscount >= 100) return
        val newCardType = CardType(cardTypeList.lastIndex + 2L, cardName, cardDiscount)
        CardTypeInteractor.addCardType(newCardType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(viewState::dismissDialog)
                .subscribeBy(
                        onComplete = { onSuccessAdd(newCardType) },
                        onError = { viewState.showMessage("FAIL")}
                )
    }

    private fun onSuccessAdd(cardType: CardType) {
        cardTypeList.add(cardType)
        viewState.addNewItem(cardType)
        viewState.showMessage(CARD_TYPE_SUCCESSFULLY_CREATED)

    }
}