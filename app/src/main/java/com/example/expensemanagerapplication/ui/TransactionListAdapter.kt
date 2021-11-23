package com.example.expensemanagerapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction

class TransactionListAdapter(private val listener: HomeFragment) : RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder>() {
    val transactions = mutableListOf<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val viewHolder = TransactionListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_cardview,parent,false))
        viewHolder.arrow.setOnClickListener {
          listener.onitemclikced(transactions[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val currentTransaction = transactions[position]
       holder.bind(currentTransaction)
    }

    override fun getItemCount(): Int {
       return  transactions.size
    }
    inner class TransactionListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val item_name = itemView.findViewById<TextView>(R.id.item)
        val item_amount = itemView.findViewById<TextView>(R.id.item_amount)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)
        val trnsaction_type_view = itemView.findViewById<View>(R.id.transaction_type_view)
        val arrow = itemView.findViewById<ImageView>(R.id.arrow)
        fun bind(transaction: Transaction) {
            item_name.text = transaction.name
            item_amount.text = transaction.amount.toString()
            item_date.text = transaction.datePicker
            when (transaction.transaction_type) {
                "Cash" -> {
                    trnsaction_type_view.setBackgroundResource(R.color.yellow)
                }
                "Debit Card" -> {
                    trnsaction_type_view.setBackgroundResource(R.color.red)
                }
                "Credit Card" -> {
                    trnsaction_type_view.setBackgroundResource(R.color.green)
                }

            }
        }
    }
    fun updatelist(newlist : List<Transaction>){
        transactions.clear()
        transactions.addAll(newlist)

        notifyDataSetChanged()
    }
}
interface ItransactionListAdapter{
    fun onitemclikced(transaction:Transaction)
}