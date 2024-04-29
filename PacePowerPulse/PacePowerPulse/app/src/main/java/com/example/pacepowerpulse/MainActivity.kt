package com.example.pacepowerpulse

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pacepowerpulse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        supportActionBar?.hide()

        //val flStartButton : FrameLayout = findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener {
            val intent=Intent(this,ExerciseActivity::class.java)
            startActivity(intent)

        }
        //Adding a click event to the BMI calculator button and navigating it to the BMI calculator feature.

        binding?.flBMI?.setOnClickListener {
            //Launching the BMI activity
            val intent=Intent(this,BMIActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}