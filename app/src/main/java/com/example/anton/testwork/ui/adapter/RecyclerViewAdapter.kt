package com.example.anton.testwork.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.anton.testwork.R
import com.example.anton.testwork.entity.CardType
import com.example.anton.testwork.entity.Card
import com.example.anton.testwork.entity.Customer
import com.example.anton.testwork.ui.viewHolder.CardTypeHolder
import com.example.anton.testwork.ui.viewHolder.CardsHolder
import com.example.anton.testwork.ui.viewHolder.CustomersHolder

class RecyclerViewAdapter <T> (private val list: ArrayList<T>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val item = list[position]
        when(item) {
            is Customer -> (holder as CustomersHolder).bind(item)
            is CardType -> (holder as CardTypeHolder).bind(item)
            is Card -> (holder as CardsHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when(list[position]) {
            is Customer -> 0
            is CardType -> 1
            else -> 2
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? =
        when(viewType) {
            0 -> CustomersHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_list_form, parent, false))
            1 -> CardTypeHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_list_form, parent, false))
            else -> CardsHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_list_form, parent, false))
        }

    fun addItem(item: T) {
        list.add(item)
        notifyDataSetChanged()
    }

    fun addAll(listItems: List<T>) {
        list.addAll(listItems)
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): T = list[pos]

    fun deleteItem(item: T) {
        list.remove(item)
        notifyDataSetChanged()
    }

    fun updateItem(item: T) {
        val oldItem = when(item) {
            is Customer -> list.find { (it as Customer).id == item.id }
            is Card -> list.find { (it as Card).id == item.id }
            is CardType -> list.find { (it as CardType).id == item.id }
            else -> null
        }
        oldItem?.let {
            val position = list.indexOf(oldItem)
            list[position] = item
            notifyItemChanged(position)
        }
    }
}