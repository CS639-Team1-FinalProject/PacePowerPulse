package com.example.pacepowerpulse

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pacepowerpulse.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

//TODO(Creating a BMI calculator activity.)
//START

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }
    private var currentVisibleView: String =
        METRIC_UNITS_VIEW // A variable to hold a value to make a selected view visible
    // END

    //create binding for the activity
    private var binding: ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflate the layout
        binding = ActivityBmiBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        // END
        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->

            // Here if the checkId is METRIC UNITS view then make the view visible else US UNITS view.
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        // START
        // Button will calculate the input values in Metric Units
        binding?.btnCalculateUnits?.setOnClickListener {

            // The values are validated.
            if (validateMetricUnits()) {

                // The height value is converted to a float value and divided by 100 to convert it to meter.
                val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                // The weight value is converted to a float value
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                // BMI value is calculated in METRIC UNITS using the height and weight value.
                val bmi = weightValue / (heightValue * heightValue)

                //display BMI result
                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BMIActivity,
                    "Please enter valid values.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
        // END
    }

    /**
    * Function is used to make the METRIC UNITS VIEW visible and hide the US UNITS VIEW.
    */
    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE // METRIC  Height UNITS VIEW is Visible
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE // METRIC  Weight UNITS VIEW is Visible
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE // make weight view Gone.
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE // make height feet view Gone.
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE // make height inch view Gone.

        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared if it is added.
        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared if it is added.

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
    }
    // END
    // TODO(Step 4 : Making a function to make the US UNITS view visible.)
    // START
    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE // METRIC  Height UNITS VIEW is InVisible
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE // METRIC  Weight UNITS VIEW is InVisible
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE // make weight view visible.
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE // make height feet view visible.
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE // make height inch view visible.

        binding?.etUsMetricUnitWeight?.text!!.clear() // weight value is cleared.
        binding?.etUsMetricUnitHeightFeet?.text!!.clear() // height feet value is cleared.
        binding?.etUsMetricUnitHeightInch?.text!!.clear() // height inch is cleared.

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
    }
    // START
    /**
     * Function is used to display the result of METRIC UNITS.
     */
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        binding?.llDiplayBMIResult?.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiValue // Value is set to TextView
        binding?.tvBMIType?.text = bmiLabel // Label is set to TextView
        binding?.tvBMIDescription?.text = bmiDescription // Description is set to TextView
    }
    // END

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

}