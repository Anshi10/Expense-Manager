package com.example.expensemanagerapplication.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.data.Transaction
import com.example.expensemanagerapplication.data.TransactionDetailDao_Impl
import com.example.expensemanagerapplication.data.Transaction_type
import com.example.expensemanagerapplication.data.Type
import com.example.expensemanagerapplication.databinding.FragmentAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {
    //binding
    private var _binding : FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    //viewModel instance
    private lateinit var addTransactionViewModel: TransactionDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTransactionViewModel = ViewModelProvider(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater,container,false)

        //handling drop down menu(Transaction Type)
        val transactionType = resources.getStringArray(R.array.type)
        val arrayAdapterT = ArrayAdapter(requireContext(), R.layout.dropdown_item, transactionType)
        binding.transactionType.setAdapter(arrayAdapterT)

        //handling Drop down menu (Categories)
        val Categories = resources.getStringArray(R.array.categories)
        val arrayAdapterC = ArrayAdapter(requireContext(),R.layout.dropdown_item,Categories)
        binding.categoryEdittext.setAdapter(arrayAdapterC)

     //on clicking back button on topBar
        binding.TopBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //on checked recurringOption
        binding.recurrence.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.fromdateLayout.isEnabled = true
                binding.todateLayout.isEnabled = true
                Log.d("anshi", "buttons is clicked")
            }
            else{
                binding.fromdateLayout.isEnabled = false
                binding.todateLayout.isEnabled = false
            }
        }
        binding.transactionDateLayout.editText?.transformIntoDatePicker(requireContext(),"dd-MM-yyyy")
        binding.fromdateLayout.editText?.transformIntoDatePicker(requireContext(),"dd-MM-yyyy")
        binding.todateLayout.editText?.transformIntoDatePicker(requireContext(),"dd-MM-yyyy")

        //if any button is clicked
        binding.expenseButton.setOnClickListener {
            storeData(Type.Expense)
        }
        binding.incomeButton.setOnClickListener {
            storeData(Type.Income)
        }

        return binding.root
    }

    private fun <E: Enum<E>> storeData(mode : E) {
        //val transactionName = binding.transactionName.text.toString()
        val amount = binding.amount.text.toString().toFloat()
        val category = binding.categoryEdittext.text.toString()
        val comment = binding.addComment.text.toString()
        val type = binding.selectTransactionLayout.editText?.text.toString()
        val date = binding.transactionDateLayout.editText?.text.toString()
        Log.d("anshi","$date in add transaction fragmnet")
        Log.d("anshi","date in store data = $date")
        var transCategory = false
        //storing date as month , date , year
        val day = Integer.parseInt(date.substring(0,2))
        val month = Integer.parseInt(date.substring(3,5))
        val year = Integer.parseInt(date.substring(6))
        //if recurrence checkbox is clicked
        var recurringFrom = binding.fromDateEdittext.text.toString()
        var recurringTo = binding.toDateEdittext.text.toString()
        if(!binding.recurrence.isChecked){
            recurringFrom = "null"
            recurringTo = "null"
        }
        if(mode==Type.Income){
            transCategory = true
        }
        val transaction = Transaction(amount,day,month,year,comment,date,type,category,recurringFrom,recurringTo,transCategory)
        addTransactionViewModel.insert(transaction)
        Log.d("anshi","data inserted in add trnasaction fragment ")
        requireActivity().onBackPressed()

        //updating shared preference
        val pref = this.requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val editor = pref.edit()
        if(mode==Type.Income){
            when(type){
                "Cash"->{
                 var cash = pref.getFloat("cash_amount",0f)
                 cash = cash+amount
                 editor.putFloat("cash_amount",cash)
                }
                "Debit Card"->{
                    var debit = pref.getFloat("debit_amount",0f)
                    debit = debit+amount
                    editor.putFloat("debit_amount",debit)
                }
                "Credit Card"->{
                    var credit = pref.getFloat("debit_amount",0f)
                     credit = credit +amount
                    editor.putFloat("debit_amount", credit)
                }
            }
        }
        if(mode==Type.Expense){
            when(type){
                "Cash"->{
                    var cash = pref.getFloat("cash_amount",0f)
                    cash = cash-amount
                    editor.putFloat("cash_amount",cash)
                }
                "Debit Card"->{
                    var debit = pref.getFloat("debit_amount",0f)
                    debit = debit-amount
                    editor.putFloat("debit_amount",debit)
                }
                "Credit Card"->{
                    var credit = pref.getFloat("debit_amount",0f)
                    credit = credit -amount
                    editor.putFloat("debit_amount", credit)
                }

            }
        }
        if(transaction.category== "Credit Card Bill"){
            var credit = pref.getFloat("credit_amount",0f)
            credit += transaction.amount
            editor.putFloat("credit_amount",credit)
        }
        editor.apply()

    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                show()
            }
        }
    }

}