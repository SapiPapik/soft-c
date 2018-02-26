package com.example.anton.testwork.ui.base

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.anton.testwork.R
import com.example.anton.testwork.ui.SwipeHelper
import com.example.anton.testwork.ui.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.list_tab_fragment.*
import org.jetbrains.anko.toast

abstract class BaseTabFragment(val title: String) : MvpAppCompatFragment() {

    var dialog: AlertDialog? = null

    abstract var adapter: RecyclerViewAdapter<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? =
            inflater.inflate(R.layout.list_tab_fragment, container, false)

    override fun onStart() {
        super.onStart()
        initSwipe()
    }

    private fun initSwipe() {
        val swipeHelper = object : SwipeHelper(context = activity, recyclerView = rvListCustomers) {
            override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder, underlayButtons: ArrayList<UnderlayButton>) {
                underlayButtons.add(SwipeHelper.UnderlayButton(
                        BitmapFactory.decodeResource(resources, R.drawable.ic_trash),
                        resources.getColor(R.color.colorRed),
                        object : SwipeHelper.UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                onClickDeleteButton(adapter.getItem(pos))
                            }
                        }
                ))
                underlayButtons.add(SwipeHelper.UnderlayButton(
                        BitmapFactory.decodeResource(resources, R.drawable.ic_edit),
                        resources.getColor(R.color.colorYellow),
                        object : SwipeHelper.UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                onClickEditButton(adapter.getItem(pos))
                            }
                        }
                ))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(rvListCustomers)
    }

    abstract fun <T> onClickDeleteButton(itemList: T)

    abstract fun <T> onClickEditButton(itemList: T)

    fun showMessage(msg: String) {
        activity?.toast(msg)
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    fun <T> addNewItem(newItemList: T) {
        (adapter as RecyclerViewAdapter<T>).addItem(newItemList)
    }

    fun <T> deleteItem(itemList: T) {
        (adapter as RecyclerViewAdapter<T>).deleteItem(itemList)
    }

    fun <T> updateItem(itemList: T) {
        (adapter as RecyclerViewAdapter<T>).updateItem(itemList)
    }

    fun <T> addListItems(listItems: List<T>) {
        (adapter as RecyclerViewAdapter<T>).addAll(listItems)
    }

    fun createAndShowDialog(dialogLayout: View, dialogTitle: String, positiveTitle: String) {
        dialog = with(AlertDialog.Builder(activity)) {
            setTitle(dialogTitle)
            setView(dialogLayout)
            setNegativeButton("Отмена", null)
            setPositiveButton(positiveTitle, null)
            create()
            show()
        }
    }

    fun resetErrorFromEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editText.text.isNotEmpty())
                    editText.error = null
            }
        })
    }
}