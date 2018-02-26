package com.example.anton.testwork.ui.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.anton.testwork.R
import com.example.anton.testwork.entity.CardType

class CardTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val fullName: TextView = itemView.findViewById(R.id.tvOne)
    private val phoneNumber: TextView = itemView.findViewById(R.id.tvTwo)

    fun bind(item: CardType) {
        fullName.text = item.name
        phoneNumber.text = item.discount.toString()
    }
}