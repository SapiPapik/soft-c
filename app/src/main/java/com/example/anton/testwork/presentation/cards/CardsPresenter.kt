package com.example.anton.testwork.presentation.cards

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.anton.testwork.model.interactor.CardInteractor
import com.example.anton.testwork.model.interactor.CardTypeInteractor
import com.example.anton.testwork.model.interactor.CustomerInteractor
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.Customer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CardsPresenter : MvpPresenter<CardView>() {
    companion object {
        const val CARD_SUCCESSFULLY_DELETED = "Запись о карте успешно удалена"
        const val CARD_SUCCESSFULLY_CREATED = "Запись о карте успешно создана"
        const val CARD_SUCCESSFULLY_EDITED = "Запись о карте успешно отредактирована"
    }

    private val cardList = ArrayList<Card>()

    private val cardTypeList = ArrayList<CardType>()

    private val customersList = ArrayList<Customer>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        CardInteractor.getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeBy(
                        onSuccess = (::onSuccessGet),
                        onError = { viewState.showMessage("FAIL") }
                )

        //TODO: not with time
        CardTypeInteractor.getCardTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeBy(
                        onSuccess = { curdTypes ->
                            cardTypeList.addAll(curdTypes)
                        },
                        onError = { viewState.showMessage("FAIL") }
                )
        //TODO: that too
        CustomerInteractor.getCustomers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeBy(
                        onSuccess = { customers ->
                            customersList.addAll(customers)
                        },
                        onError = {
                            viewState.showMessage("FAIL")
                        }
                )
    }

    private fun onSuccessGet(cards: List<Card>) {
        cardList.addAll(cards)
        viewState.showCardList(cardList)
    }

    fun onClickDeleteButton(card: Card) {
        CardInteractor.deleteCardById(card.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { onSuccessDeleteCard(card) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessDeleteCard(card: Card) {
        cardList.remove(card)
        viewState.deleteItem(card)
        viewState.showMessage(CARD_SUCCESSFULLY_DELETED)
    }

    fun onClickAddNewCard() {
        viewState.showCreateNewCardDialog(cardTypeList, customersList)
    }

    fun onClickEditButton(card: Card) {
        viewState.showEditCardDialog(card, cardTypeList, customersList)
    }

    fun onClickCreateButton(cardNumber: String, cardType: CardType, customer: Customer? = null) {
        val newCard = Card(2L, cardNumber, cardType, customer)
        CardInteractor.addCard(newCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(viewState::dismissDialog)
                .subscribeBy(
                        onComplete = { onSuccessAddCard(newCard) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessAddCard(card: Card) {
        cardList.add(card)
        viewState.addNewItem(card)
        viewState.showMessage(CARD_SUCCESSFULLY_CREATED)
    }

    fun onClickEditCard(newCard: Card) {
        CardInteractor.updateCard(newCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(viewState::dismissDialog)
                .subscribeBy(
                        onComplete = { onSuccessAddCard(newCard) },
                        onError = { viewState.showMessage("FAIL") }
                )
    }

    private fun onSuccessEditCard(newCard: Card) {
        val pos = cardList.indexOf(cardList.find { it.id == newCard.id })
        cardList[pos] = newCard
        viewState.updateItem(newCard)
        viewState.showMessage(CARD_SUCCESSFULLY_EDITED)
    }
}