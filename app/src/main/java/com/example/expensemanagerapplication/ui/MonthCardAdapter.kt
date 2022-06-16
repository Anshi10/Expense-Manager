package com.example.expensemanagerapplication.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.MonthName
import kotlin.random.Random

class MonthCardAdapter(private  val listener : ViewBy) : RecyclerView.Adapter<MonthViewHolder>() {
    var monthList = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
          val viewholder = MonthViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month_card_view,parent,false))
        viewholder.viewAll.setOnClickListener {
           listener.onMonthCardClicked(monthList[viewholder.adapterPosition])
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
         holder.bind(monthList[position])
    }

    override fun getItemCount(): Int {
        return monthList.size
    }
    fun updateMonthList(newlist : List<Int>){
        monthList.clear()
        monthList.addAll(newlist)

        notifyDataSetChanged()
    }
}
class MonthViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView){
    val monthName = itemView.findViewById<TextView>(R.id.month)
    val viewAll = itemView.findViewById<View>(R.id.Montharrow)
    val monthCard = itemView.findViewById<CardView>(R.id.monthCard)
    fun bind(transaction: Int){
        for(month in MonthName.values()){
            if(month.ordinal==(transaction-1))
                monthName.text = month.name
        }
        val random = Random
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        monthCard.setCardBackgroundColor(color)
    }
}

interface IMonthCardListAdapter{
    fun onMonthCardClicked(transaction: Int)
}
