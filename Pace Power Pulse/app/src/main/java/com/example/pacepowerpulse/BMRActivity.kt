package com.example.pacepowerpulse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BMRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmr)

        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val resultText = findViewById<TextView>(R.id.resultText)
        val maleRadioButton = findViewById<RadioButton>(R.id.maleRadioButton)
        val femaleRadioButton = findViewById<RadioButton>(R.id.femaleRadioButton)

        calculateButton.setOnClickListener {
            val age = ageInput.text.toString().toInt()
            val weight = weightInput.text.toString().toDouble()
            val height = heightInput.text.toString().toDouble()

            val bmr: Double = if (maleRadioButton.isChecked) {
                66 + (13.75 * weight) + (5.003 * height) - (6.755 * age)
            } else {
                655 + (9.563 * weight) + (1.850 * height) - (4.676 * age)
            }

            resultText.text = String.format("Your Basal Metabolic Rate (BMR) is: %.2f", bmr)
        }
    }
}