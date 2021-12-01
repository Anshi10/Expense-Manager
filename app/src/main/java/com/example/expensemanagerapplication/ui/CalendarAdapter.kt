package com.example.expensemanagerapplication.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction

class CalendarAdapter() : RecyclerView.Adapter<calendarViewHolder>() {
    val allTransactions = mutableListOf<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): calendarViewHolder {
        val viewHolder = calendarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_view,parent,false))
        return  viewHolder
    }

    override fun onBindViewHolder(holder: calendarViewHolder, position: Int) {
           holder.bind(allTransactions[position])
    }

    override fun getItemCount(): Int {
        return allTransactions.size
    }
    fun updatelist(newlist : List<Transaction>){
        allTransactions.clear()
        allTransactions.addAll(newlist)

        notifyDataSetChanged()
    }
}

class calendarViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
          val transactionName = itemView.findViewById<TextView>(R.id.itemC)
          val transactionAmount = itemView.findViewById<TextView>(R.id.item_amountC)
    fun bind(transaction : Transaction){
        transactionName.setText(transaction.name)
        if(transaction.IorE){
            transactionAmount.text = "+"+transaction.amount.toString()
            transactionAmount.setTextColor(Color.parseColor("#07e642"))
        }
        else{
            transactionAmount.text = "-"+transaction.amount.toString()
            transactionAmount.setTextColor(Color.parseColor("#f71f07"))
        }
    }
}
