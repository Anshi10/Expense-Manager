package com.example.expensemanagerapplication.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.expensemanagerapplication.data.MoneyDatabase
import com.example.expensemanagerapplication.data.Transaction
import com.example.expensemanagerapplication.data.TransactionDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailViewModel(application: Application) :AndroidViewModel(application) {
    private val transactionDetailRepository : TransactionDetailRepository
    var transactions : LiveData<List<Transaction>>
    init{
        val dao = MoneyDatabase.getDatabase(application).transactionListDao()
        transactionDetailRepository  = TransactionDetailRepository(dao)
        transactions = transactionDetailRepository.updateTransaction()
    }

    fun delete(transaction:Transaction) = viewModelScope.launch(Dispatchers.IO ){
        transactionDetailRepository.delete(transaction)
        Log.d("anshi","TDV transaction deleted")
    }
    fun insert(transaction:Transaction) = viewModelScope.launch(Dispatchers.IO){
        transactionDetailRepository.insert(transaction)
        Log.d("anshi","TDV transaction inserted")
        updateTrans()
    }
    fun updateTrans() {
        transactions = transactionDetailRepository.updateTransaction()
    }
}