package com.example.pacepowerpulse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pacepowerpulse.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?= null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        print("current user is " + firebaseAuth.currentUser)
        Log.d("current user is ", firebaseAuth.currentUser?.email.toString())
        // Check if the user is signed in
        if (firebaseAuth.currentUser == null) {
            // If not signed in, launch SignInActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
           finish() // Finish the MainActivity to prevent going back to it after signing in
        }
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
        binding?.flBMR?.setOnClickListener {
            //Launching the BMR activity
            val intent=Intent(this,BMRActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        FirebaseAuth.getInstance().signOut()
//        firebaseAuth.signOut()
       firebaseAuth.currentUser?.delete()
    }
}