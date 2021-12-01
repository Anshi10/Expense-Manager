package com.example.expensemanagerapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction

class MonthCardAdapter : RecyclerView.Adapter<MonthViewHolder>() {
    val monthList = mutableListOf<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
          val viewholder = MonthViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month_card_view,parent,false))
        return viewholder
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
         holder.bind(monthList[position])
    }

    override fun getItemCount(): Int {
        return 3
    }
}

class MonthViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView){
    val monthName = itemView.findViewById<TextView>(R.id.month)
    val budgetStatus = itemView.findViewById<TextView>(R.id.budgetExceeded)
    val viewAll = itemView.findViewById<View>(R.id.Montharrow)
    fun bind(transaction: Transaction){
        monthName.setText(transaction.month.toString())
    }
}
