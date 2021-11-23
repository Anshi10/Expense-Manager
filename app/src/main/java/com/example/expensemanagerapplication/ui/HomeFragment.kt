package com.example.expensemanagerapplication.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction
import com.example.expensemanagerapplication.databinding.FragmentHomeBinding
import org.eazegraph.lib.models.PieModel


class HomeFragment : Fragment(),ItransactionListAdapter {
    //binding
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //view Model instance
    private lateinit var HomeFragmentViewModel : TransactionDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
 HomeFragmentViewModel = ViewModelProvider(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }
        binding.setBalInfo.setOnClickListener {
            showDialog()
        }
        AppBar()
        updatePiechart()
        upcomingTransaction()
    }

    private fun upcomingTransaction() {
       binding.transCardRV.layoutManager = LinearLayoutManager(activity)
        val adapter =TransactionListAdapter(this)
       binding.transCardRV.adapter = adapter
        HomeFragmentViewModel.transactions.observe(viewLifecycleOwner,{ list->
            run {
                list?.let {
                    Log.d("ansi",it.size.toString())
                    adapter.updatelist(it)
                }
            }
        })
//        HomeFragmentViewModel.transactions.observe(viewLifecycleOwner, Observer{
//            (trans_cardRV.adapter as TransactionListAdapter).submitList(it)
//        })
        Log.d("anshi","upcoming transaction implemented")
    }

    private fun AppBar() {
            binding.topbar.setOnMenuItemClickListener{menuItem->
                when(menuItem.itemId){
                    R.id.calender_icon->{
                        findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
                        true
                    }
                    R.id.profile->{
                        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                        true
                    }
                    else->false
                }
            }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        //inflate set_balance_info layout
        val dialogView =  LayoutInflater.from(requireContext()).inflate(R.layout.set_balance_info, null)
        val cash = dialogView.findViewById<EditText>(R.id.cashAmount)
        val credit = dialogView.findViewById<EditText>(R.id.creditAmount)
        val debit = dialogView.findViewById<EditText>(R.id.bankAmount)
        with(builder){
            setView(dialogView)
            setTitle("Set Details")
            setMessage("Set Cash distribution Information")
            setPositiveButton("Set") { dialog, which ->
                if (cash.text.toString() == "" || debit.text.toString() == "" || credit.text.toString() =="") {
                    Toast.makeText(activity, "Invalid Input ", Toast.LENGTH_SHORT).show()
                }else {
                    val cash_amount = cash.text.toString().toFloat()
                    val debit_amount = debit.text.toString().toFloat()
                    val credit_amount = credit.text.toString().toFloat()
                    setBalanceInfo(cash_amount,debit_amount,credit_amount)
                }
            }
            setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                }
            })
            val alertdialog = builder.create()
            alertdialog.show()
        }
    }

    private fun setBalanceInfo(cashAmount: Float, debitAmount: Float, creditAmount: Float) {
        val pref = requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putFloat("cash_amount",cashAmount)
        editor.putFloat("debit_amount",debitAmount)
        editor.putFloat("credit_amount",creditAmount)
        editor.apply()
        //Log.d("anshi","all amount set")
        updatePiechart()
    }

    private fun updatePiechart() {
        val pref = requireActivity().getSharedPreferences("Preference",Context.MODE_PRIVATE)
        val cash_amount = pref.getFloat("cash_amount",0f)
        val debit_amount= pref.getFloat("debit_amount",0f)
        val credit_amount = pref.getFloat("credit_amount",0f)
//        Log.d("anshi","$cash_amount")
//        Log.d("anshi","$debit_amount")
//        Log.d("anshi","$credit_amount")
        //showing values
        binding.cashAmount.text= "$cash_amount"
        binding.debitAmount.text= "$debit_amount"
        binding.creditAmount.text = "$credit_amount"
        var sum = cash_amount+debit_amount+credit_amount
        binding.totalAmount.text = "$sum"
        binding.piechart.clearChart()
        //adding Piechart
        binding.piechart?.addPieSlice(
            PieModel("Cash",cash_amount, Color.parseColor("#FFA726"))
        )
        binding.piechart?.addPieSlice(
            PieModel("Credit", credit_amount, Color.parseColor("#66BB6A"))
        )
        binding.piechart?.addPieSlice(
            PieModel("Dredit", debit_amount, Color.parseColor("#EF5350"))
        )
        binding.piechart?.startAnimation();
    }

    override fun onitemclikced(transaction: Transaction) {

    }

}
