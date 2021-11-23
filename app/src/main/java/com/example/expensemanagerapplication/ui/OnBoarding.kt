package com.example.expensemanagerapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensemanagerapplication.R
import com.example.expensemanagerapplication.databinding.FragmentOnBoardingBinding

class OnBoarding : Fragment(R.layout.fragment_on_boarding) {
    private var _binding :FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = requireActivity().getSharedPreferences("Preference",Context.MODE_PRIVATE)
        val FirstTimeOpened : Boolean = pref.getBoolean("FirstTimeOpened",true)
        if(FirstTimeOpened){
            Log.d("anshi","first time opening")
            val editor = pref.edit()
            editor.putBoolean("FirstTimeOpened",false)
            editor.apply()
        }else{
            findNavController().navigate(R.id.action_onBoarding_to_homeFragment)
            Log.d("anshi","already first time opened")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        binding.continueButton.setOnClickListener {
            if (binding.personName.text.toString() == "" || binding.budget.text.toString() == "" || binding.monthlyIncome.text.toString() == "") {
                Toast.makeText(activity, "Please fill all Fields", Toast.LENGTH_SHORT).show()
            } else {
                val name = binding.personName.editableText.toString()
                val monthly_budget = binding.budget.editableText.toString().toFloat()
                val income = binding.monthlyIncome.editableText.toString().toFloat()
                editor.putString("name", name)
                editor.putFloat("monthly_budget", monthly_budget)
                editor.putFloat("income", income)
                editor.apply()

                findNavController().navigate(R.id.action_onBoarding_to_homeFragment)
            }
        }

    }
}