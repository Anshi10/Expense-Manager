package com.example.expensemanagerapplication.data

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
    fun getTransactionRY(Id : Long) : Transaction{
        return transactionDetailDao.getTransactionDO(Id)
    }
}