package com.example.expensemanagerapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Transaction_type(){
    Cash , debit , Credit
}
enum class Type(){
    Income, Expense
}

@Entity(tableName = "transaction_table")
data class Transaction(
    val name : String,
    val amount : Float,
    val day : Int,
    val month : Int,
    val year : Int,
    val comment: String,
    val datePicker: String,
    val transaction_type : String,
    val category : String,
    val recurring_from : String,
    val recurring_to : String
){
    @PrimaryKey(autoGenerate = true) var id :Long=0
}
