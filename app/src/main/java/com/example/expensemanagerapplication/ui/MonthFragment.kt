package com.example.expensemanagerapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.databinding.FragmentHomeBinding
import com.example.expensemanagerapplication.databinding.FragmentMonthBinding

class MonthFragment : Fragment() {
    //binding
    private var _binding : FragmentMonthBinding? = null
    private val binding get() = _binding!!
    //view Model instance
    private lateinit var MonthFragmentViewModel : TransactionDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MonthFragmentViewModel = ViewModelProvider(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMonthBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //handling back button
        binding.monthAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //setting budget
        val pref : SharedPreferences = this.requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val budget = pref.getFloat("monthly_budget",0f)
        binding.monthlyBudget.text = budget.toString()
        //handling recycler view
        binding.Payments.layoutManager = LinearLayoutManager(activity)
        val adapter = TransactionListAdapter(HomeFragment())
        binding.Payments.adapter = adapter
        //getting month value
       val args : MonthFragmentArgs by navArgs()
        val monthChoosen = args.monthNo
        //variable to store spent amount
        var amtSpent = 0f
        //observe live data by this adapter
        MonthFragmentViewModel.getTransactionbyMonth(monthChoosen).observe(viewLifecycleOwner) { list ->
            run {
                list?.let {
                    Log.d("anshi", it.size.toString())
                    adapter.updatelist(it)
                    for(i in 0..it.size-1){
                      amtSpent +=  it[i].amount
                    }
                    Log.d("anshi","amtSpent updated"+amtSpent)
                    //setting amount spent and amount saved
                        binding.spentAmount.text = amtSpent.toString()
                    binding.savedAmount.text = (budget-amtSpent).toString()
                }
            }
        }
    }
}