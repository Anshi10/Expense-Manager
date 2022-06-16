package com.example.expensemanagerapplication.data

import android.util.Log
import androidx.lifecycle.LiveData

class TransactionDetailRepository(private  val transactionDetailDao : TransactionDetailDao) {
    suspend fun insert(transaction: Transaction){
        transactionDetailDao.insert(transaction)
    }
    suspend fun delete(transaction:Transaction){
        transactionDetailDao.delete(transaction)
    }
    suspend fun update(transaction: Transaction){
        transactionDetailDao.update(transaction)
    }
    fun updateTransaction() : LiveData<List<Transaction>> {
        return transactionDetailDao.getalltransaction()
    }
    fun getTransactionbyID(Id : Long) : Transaction{
        return transactionDetailDao.getTransactionbyId(Id)
    }
    fun getTransactionbydate(date : String):LiveData<List<Transaction>>{
        val result =  transactionDetailDao.getTransactionbyDate(date)
        return  result
    }
    fun getTransactionbytype(type : String) : LiveData<List<Transaction>>{
        val ans = transactionDetailDao.getTransactionbyType(type)
        return ans
    }
    fun getTransactionbyMonth(MonthNo : Int) : LiveData<List<Transaction>>{
        val monthans = transactionDetailDao.getTransactionbyMonth(MonthNo)
        return monthans
    }
    fun getMonthList() : LiveData<List<Int>>{
        val MonthListans = transactionDetailDao.getMonthList()
        return MonthListans
    }
}