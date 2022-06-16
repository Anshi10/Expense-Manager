package com.example.expensemanagerapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Transaction_type(){
    Cash , debit , Credit
}
enum class Type(){
    Income, Expense
}
enum class MonthName(val monthNum : Int) {
    JANUARY (1),
    FEBRUARY (2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12)
}

@Entity(tableName = "transaction_table")
data class Transaction(
    //val name : String,
    val amount : Float,
    val day : Int,
    val month : Int,
    val year : Int,
    val comment: String,
    val datePicker: String,
    val transaction_type : String,
    val category : String,
    val recurring_from : String,
    val recurring_to : String,
    val IorE : Boolean
){
    @PrimaryKey(autoGenerate = true) var id :Long=0
}
//true : Income
//false : Expense
