package com.example.pacepowerpulse

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pacepowerpulse.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
        private var binding: ActivityExerciseBinding?=null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityExerciseBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            setContentView(binding?.root)
            setSupportActionBar(binding?.exerciseToolbar)

            if(supportActionBar!=null){
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            binding?.exerciseToolbar?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

}
