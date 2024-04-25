package com.example.pacepowerpulse

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pacepowerpulse.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }
    private var currentVisibleView : String =
        METRIC_UNITS_VIEW


    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{_, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsMetricUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener{
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?. tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?. tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?. tilUsMetricUnitWeight?.visibility = View.GONE
        binding?. tilUsMetricUnitHeightFeet?.visibility = View.GONE
        binding?. tilUsMetricUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsMetricUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?. tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?. tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?. tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?. tilUsMetricUnitHeightFeet?.visibility = View.VISIBLE
        binding?. tilUsMetricUnitHeightInch?.visibility = View.VISIBLE

        binding?.etMetricUsUnitWeight?.text!!.clear()
        binding?.etMetricUsUnitHeightFeet?.text!!.clear()
        binding?.etMetricUsUnitHeightInch?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi : Float){

        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severly underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "severly underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "underweight"
            bmiDescription ="Oops! You really need to take better care of yourself! Eat More!"
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription ="Congratulations! You are in a good shape!"
        }
        else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) >= 0){
            bmiLabel = "Overweight"
            bmiDescription ="Oops! You really need to take better care of yourself! Workout more!"
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderate obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severly obese)"
            bmiDescription = "OMG! You are in very dangerous conditions! Act now!"
        }else{
            bmiLabel = "Obese Class ||| (Very Severly Obese)"
            bmiDescription = "OMG You are in dangerous conditions! Act Now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()


        binding?.llDiplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun validateMetricUnits():Boolean{
        var isValid =true

        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResult(bmi)
            }else{
                Toast.makeText(
                    this@BMIActivity,
                    "Please enter valid values.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }else{
            if(validateUsUnits()){
                val usUnitHeightValueFeet : String =
                    binding?.etMetricUsUnitHeightFeet?.text.toString()
                val usUnitHeightValueInch : String =
                    binding?.etMetricUsUnitHeightInch?.text.toString()
                val usUnitWeightValue : Float =
                    binding?.etMetricUsUnitWeight?.text.toString().toFloat()

                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue *heightValue))

                displayBMIResult(bmi)
            }else{
                Toast.makeText(
                    this@BMIActivity,
                    "Please enter valid values.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun validateUsUnits(): Boolean {
        var isValid= true

        when{
            binding?.etMetricUsUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etMetricUsUnitHeightFeet?.text.toString().isEmpty()-> {
                isValid = false
            }
            binding?.etMetricUsUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}

