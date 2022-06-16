package com.example.expensemanagerapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.databinding.FragmentAddTransactionBinding
import com.example.expensemanagerapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.profileTopbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        //on edit button clicked
        binding.profileTopbar.setOnMenuItemClickListener{menuItem->
            when(menuItem.itemId){
                R.id.edit-> {enabled()
                    true}
                else->false
            }
        }
        //getting details of profile
        val pref = this.requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val name = pref.getString("name",null)
        val monthlyBudget = pref.getFloat("monthly_budget",0f)
        val monthlyIncome = pref.getFloat("income",0f)
        //setting values
        setValues(name,monthlyBudget,monthlyIncome)
        //update values
        updateValues()
    }

    private fun updateValues() {
        val pref = this.requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val editor = pref.edit()
        binding.saveButton.setOnClickListener{
            editor.putString("name",binding.Name.text?.trim().toString())
            editor.putFloat("monthly_budget",binding.budget.text.toString().toFloat())
            editor.putFloat("income",binding.income.text.toString().toFloat())
            editor.commit()
            disabled()
        }
    }

    private fun enabled() {
        binding.cardLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.Name.isEnabled = true
        binding.budget.isEnabled = true
        binding.income.isEnabled = true
        binding.saveButton.isEnabled = true

    }
    private fun disabled() {
        binding.cardLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.lightGrey))
        binding.Name.isEnabled = false
        binding.budget.isEnabled = false
        binding.income.isEnabled = false
        binding.saveButton.isEnabled = false

    }
    private fun setValues(name: String?,monthly_budget : Float, monthly_income: Float) {
        //setting values
        if(name!=null) {
           binding.Name.setText(name)
        }
        binding.budget.setText(monthly_budget.toString())
       binding.income.setText(monthly_income.toString())
    }

}