package com.example.expensemanagerapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanagerapplication.databinding.FragmentCalendarBinding


class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
   private  lateinit var calenderviewModel : TransactionDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     calenderviewModel = ViewModelProvider(this).get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)

        //handling backbutton
        binding.calenderAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        //storing chosen date in a field
        binding.calender.setOnDateChangeListener { _, year, month, date ->
            val Month = month + 1
            var Date : String = "$date-$Month-$year"
            if(date<10){
                Date = "0$date-$Month-$year"
            }
            if(month<10){
                Date = "$date-0$Month-$year"
            }
            Toast.makeText(activity, Date, Toast.LENGTH_SHORT).show()

            //handling recycler view
            binding.calenderRV.layoutManager = LinearLayoutManager(activity)
            val adapter = CalendarAdapter()
            binding.calenderRV.adapter = adapter
            calenderviewModel.getTransactionbydate(Date).observe(viewLifecycleOwner, { list ->
                run {
                    list?.let {
                        Log.d("ansi", it.size.toString())
                        Log.d("anshi", "calender RV implemented")
                        adapter.updatelist(it)
                    }
                }
            })
        }
        return binding.root
    }

}