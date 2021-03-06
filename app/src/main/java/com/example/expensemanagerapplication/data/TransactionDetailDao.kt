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
    fun getTransactionbyId(Id : Long): Transaction

    @Query("select * from transaction_table where datePicker = :date")
    fun getTransactionbyDate(date : String) : LiveData<List<Transaction>>

    @Query("select * from transaction_table where transaction_type = :type")
    fun getTransactionbyType(type : String) : LiveData<List<Transaction>>

    @Query("select * from transaction_table where month = :MonthNo")
    fun getTransactionbyMonth(MonthNo : Int) : LiveData<List<Transaction>>

    @Query("select DISTINCT month from transaction_table")
    fun  getMonthList() : LiveData<List<Int>>
}