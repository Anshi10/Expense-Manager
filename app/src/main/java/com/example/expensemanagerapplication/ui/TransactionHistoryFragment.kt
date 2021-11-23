package com.example.expensemanagerapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionHistoryBinding.inflate(inflater,container,false)
        //when back button is pressed
        binding.TopBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //disable all views
        disabled()
        //getting id from Home Fragment
        val args: TransactionHistoryFragmentArgs by navArgs()
        val Id = args.id
        //setting data
        val transaction = viewModel.getTransactionVM(Id)
        setData(transaction)
        //handling edit button
        handleMenuButtons(Id,transaction)
        return binding.root
    }

    private fun disabled() {
        binding.transactionName.isEnabled = false
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

    private fun handleMenuButtons(Id:Long,transaction: Transaction) {
      binding.TopBar.setOnMenuItemClickListener { it->
          when(it.itemId){
                  R.id.edit->{
                      abled()
                      it.setIcon(R.id.check)
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

    private fun abled() {
        binding.transactionName.isEnabled = true
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

    private fun setData(transaction: Transaction) {
     binding.transactionName.setText(transaction.name)
        binding.amount.setText(transaction.amount.toString())
        binding.date.setText(transaction.datePicker)
        binding.categoryEdittext.setText(transaction.category)
        binding.transactionType.setText(transaction.transaction_type)
        if(transaction.IorE){
            binding.income.isChecked=true
        }
        else{
            binding.Expense.isChecked= true
        }
    }


}