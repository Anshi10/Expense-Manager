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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater,container,false)
        //handling drop down menu
        val transactionType = resources.getStringArray(R.array.type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, transactionType)
        binding.transactionType.setAdapter(arrayAdapter)
     //on clicking back button on topBar
        binding.TopBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //on checked recurringOption
        binding.recurrence.setOnCheckedChangeListener { buttonView, isChecked ->
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
        val transactionName = binding.transactionName.text.toString()
        val amount = binding.amount.text.toString().toFloat()
        val category = binding.categoryEdittext.text.toString()
        val comment = binding.addComment.text.toString()
        val type = binding.selectTransactionLayout.editText?.text.toString()
        var date = binding.transactionDateLayout.editText?.text.toString()
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
        val transaction = Transaction(transactionName,amount,day,month,year,comment,date,type,category,recurringFrom,recurringTo,transCategory)
        addTransactionViewModel.insert(transaction)
        Log.d("anshi","data inserted in add trnasaction fragment ")
        requireActivity().onBackPressed()

    }

    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
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
//                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

}