package com.example.pacepowerpulse

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pacepowerpulse.databinding.ActivityBmiBinding

//TODO(Step 2 : Creating a BMI calculator activity.)
//START

class BMIActivity : AppCompatActivity() {
    //Todo 3 create binding for the activity
    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Todo 4 inflate the layout
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
    }
}