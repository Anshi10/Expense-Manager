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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

       binding.topbar.setNavigationOnClickListener {
           //opening navigation drawer
          binding.drwarerLayout.openDrawer(GravityCompat.START)
       }
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
        updatePiechart()
        AppBar()
        updatePiechart()
       upcomingTransaction()

    }


    private fun upcomingTransaction() {
        Log.d("anshi" ,"upcoming tarnsaction")
       binding.transCardRV.layoutManager = LinearLayoutManager(activity)
        val adapter =TransactionListAdapter(this)
       binding.transCardRV.adapter = adapter
        //budget amount variable
        var budgetAmt = 0f
        //total budget
        val pref = requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val totalBudget =  pref.getFloat("monthly_budget",0f)
        HomeFragmentViewModel.transactions.observe(viewLifecycleOwner) { list ->
            run {
                list?.let {
                    Log.d("ansi", it.size.toString())
                    adapter.updatelist(it)
                    for (i in it.indices) {
                        budgetAmt += it[i].amount
                    }
                    //setting it to budegt amount text
                    binding.BudgetText.text = "Remaining Budget : " + (totalBudget-budgetAmt).toString()
                }
            }
        }
    }

    private fun AppBar() {
        binding.navView.setNavigationItemSelectedListener {menuItem->
            when(menuItem.itemId){
                R.id.DailyBasis->{
                    findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
                    true
                }
                R.id.MonthlyBasis->{
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewBy("Month"))
                    true
                }
//                R.id.categoryWise->{
//                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewBy("Category"))
//                    true
//                }
                R.id.TransactionTypeBasis->{
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewBy("TransactionType"))
                    true
                }
//                R.id.profile_navHeader->{
//                    Log.d("msg","profile icon clicked")
//                    findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
//                    true
//                }
                R.id.navFeedback->{
                    collectFeedback()
                    true
                }
                R.id.help->{
                    showHelpMessage()
                    true
                }
                else->false
            }
        }
        //handling Nav_Header clicks
        val header = binding.navView.getHeaderView(0)
        val profile = header.findViewById<ImageView>(R.id.profile_navHeader)
        profile.setOnClickListener {
            Log.d("msg","profile icon clicked")
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        //setting person Name in Nav header
        val personName = header.findViewById<TextView>(R.id.personName)
        val pref = this.requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        personName.text = pref.getString("name",null)
        //handling camera floating button
//        val addImage = header.findViewById<FloatingActionButton>(R.id.ImagechangeBtn)
//        addImage.setOnClickListener{
//           ImagePicker.with(this)
//                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .start()
//        }

//            binding.topbar.setOnMenuItemClickListener{menuItem->
//
//            }
    }

    private fun showHelpMessage() {
        val builder =  AlertDialog.Builder(requireActivity())
        with(builder){
            setTitle("How to use this App")
            setMessage("This is a simple and easy to use app for managing expenses. Add your money distribution by clicking on set balance info button.To add a transaction , simply click on add floating button present on home screen.Then view all your transactions on the basis of daily , monthly and payment type.")

        }
        val alertdialog = builder.create()
        alertdialog.show()
    }

    private fun collectFeedback() {
            val builder =  AlertDialog.Builder(requireActivity())
            //inflate feedback form layout
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.feedback_form,null)
            val feedback = dialogView.findViewById<EditText>(R.id.feedbackForm)
            with(builder){
                setView(dialogView)
                setTitle("Feedback Form")
                setMessage("\nShare your Feedback here")
                setPositiveButton("Submit"){_,_ ->
                    val feedbackMsg = feedback.text.toString()
                    if(feedbackMsg==""){
                        Toast.makeText(activity, "Invalid Input ", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val pref = requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("feedback" , feedbackMsg )
                        editor.apply()
                    }
                }
            }
        val alertdialog = builder.create()
        alertdialog.show()
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
            setPositiveButton("Set") { _, _ ->
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
        val sum = cash_amount+debit_amount+credit_amount
        binding.totalAmount.text = "$sum"
        binding.piechart.clearChart()
        //adding Piechart
        binding.piechart.addPieSlice(
            PieModel("Cash",cash_amount, Color.parseColor("#07e642"))
        )
        binding.piechart.addPieSlice(
            PieModel("Credit", credit_amount, Color.parseColor("#afb806"))
        )
        binding.piechart.addPieSlice(
            PieModel("Debit", debit_amount, Color.parseColor("#EF5350"))
        )
        binding.piechart.startAnimation()
    }

    override fun onitemclikced(transaction: Transaction) {
          findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransactionHistoryFragment(transaction.id))
    }

}
