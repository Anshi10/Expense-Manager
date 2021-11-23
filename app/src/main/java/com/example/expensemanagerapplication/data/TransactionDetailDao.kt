package com.example.expensemanagerapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDetailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction:Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Query("SELECT * FROM transaction_table ORDER BY id ASC")
    fun getalltransaction(): LiveData<List<Transaction>>

    @Query("select * from transaction_table where id = :Id")
    fun getTransactionDO(Id : Long): Transaction
}