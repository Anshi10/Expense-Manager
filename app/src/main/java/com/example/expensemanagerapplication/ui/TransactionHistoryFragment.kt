package com.example.expensemanagerapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction
import com.example.expensemanagerapplication.data.Type
import com.example.expensemanagerapplication.databinding.FragmentTransactionHistoryBinding

class TransactionHistoryFragment : Fragment() {
    //binding instance
    private var _binding : FragmentTransactionHistoryBinding? = null
    private val binding get() = _binding!!
    //Transaction Detail View Model instance
    private lateinit var  viewModel : TransactionDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         viewModel = ViewModelProvider(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentTransactionHistoryBinding.inflate(inflater,container,false)
        //when back button is pressed
        binding.TopBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //disable all views
        disabled()
        //getting id from Home Fragment
        val args : TransactionHistoryFragmentArgs by navArgs()
        val Id = args.id
        //setting data
        val transaction = viewModel.getTransactionbyid(Id)
        setData(transaction)
        //handling edit button
        handleMenuButtons(transaction)
        return binding.root
    }
    private fun setData(transaction: Transaction) {
        binding.amount.setText(transaction.amount.toString())
        binding.date.setText(transaction.datePicker)
        binding.categoryEdittext.setText(transaction.category)
        binding.transactionType.setText(transaction.transaction_type)
        binding.addComment.setText(transaction.comment)
        if(transaction.IorE){
            binding.income.isChecked=true
        }
        else{
            binding.Expense.isChecked= true
        }
    }
    private fun disabled() {
        binding.amount.isEnabled= false
        binding.dateLayout.isEnabled=false
        binding.recurrenceOption.isEnabled=false
        binding.fromdateLayout.isEnabled= false
        binding.todateLayout.isEnabled=false
        binding.categoryLayout.isEnabled=false
        binding.selectTransactionLayout.isEnabled=false
        binding.commentLayout.isEnabled= false
        binding.income.isEnabled = false
        binding.Expense.isEnabled = false
    }

    private fun handleMenuButtons(transaction: Transaction) {
      binding.TopBar.setOnMenuItemClickListener { it->
          when(it.itemId){
                  R.id.edit->{
                      abled()
                      it.setIcon(R.drawable.check)
                      it.setOnMenuItemClickListener{MenuItem->
                          when(MenuItem.itemId){
                              R.id.edit->{
                                  val freshTrans = storefreshdata()
                                  viewModel.update(freshTrans)
                                 disabled()
                                  requireActivity().onBackPressed()
                                  true
                              }
                              else->false
                          }
                      }
                      true
                  }
              R.id.delete->{
                  viewModel.delete(transaction)
                  requireActivity().onBackPressed()
                   true
              }
              else->false
          }
      }
    }

    private fun storefreshdata() : Transaction {
        val amount = binding.amount.text.toString().toFloat()
        val category = binding.categoryEdittext.text.toString()
        val comment = binding.addComment.text.toString()
        val type = binding.selectTransactionLayout.editText?.text.toString()
        var date = binding.dateLayout.editText?.text.toString()
        Log.d("anshi","date in store data = $date")
        var transCategory = false
        //storing date as month , date , year
        val day = Integer.parseInt(date.substring(0,2))
        val month = Integer.parseInt(date.substring(3,5))
        val year = Integer.parseInt(date.substring(6))
        //if recurrence checkbox is clicked
        var recurringFrom = binding.fromDateEdittext.text.toString()
        var recurringTo = binding.toDateEdittext.text.toString()
        if(!binding.recurrenceOption.isChecked){
            recurringFrom = "null"
            recurringTo = "null"
        }
       if(binding.income.isChecked){
           transCategory
       }
        val freshtransaction = Transaction(amount,day,month,year,comment,date,type,category,recurringFrom,recurringTo,transCategory)
     return freshtransaction
    }

    private fun abled() {
        binding.amount.isEnabled= true
        binding.dateLayout.isEnabled=true
        binding.recurrenceOption.isEnabled=true
        binding.fromdateLayout.isEnabled= true
        binding.todateLayout.isEnabled=true
        binding.categoryLayout.isEnabled=true
        binding.selectTransactionLayout.isEnabled=true
        binding.commentLayout.isEnabled= true
        binding.income.isEnabled = true
        binding.Expense.isEnabled = true
    }

}
