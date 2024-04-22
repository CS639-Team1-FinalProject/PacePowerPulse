package com.example.pacepowerpulse

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pacepowerpulse.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
        private var binding: ActivityExerciseBinding?=null

        private var restTimer: CountDownTimer? =null
        private var restProgress = 0

        private var exerciseTimer: CountDownTimer? =null
        private var exerciseProgress = 0

        private var exerciseList : ArrayList<ExerciseModel>?= null
        private var currentExercisePosition = -1


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityExerciseBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            setContentView(binding?.root)
            setSupportActionBar(binding?.exerciseToolbar)

            if(supportActionBar!=null){
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            exerciseList = Constants.defaultExerciseList()

            binding?.exerciseToolbar?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            setupRestView()

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    private fun setupRestView(){
        binding?.flRestview?.visibility= View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility =View.VISIBLE


        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition +1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flRestview?.visibility= View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility =View.INVISIBLE

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress =0
        }
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
      binding?.progressbar?.progress = restProgress

      restTimer = object:CountDownTimer(1000,1000){
          override fun onTick(millisUntilFinished: Long) {
              restProgress++
              binding?.progressbar?.progress = 10 - restProgress
              binding?.tvTimer?.text = (10 - restProgress).toString()
          }

          override fun onFinish() {
              currentExercisePosition++
              setupExerciseView()
          }

      }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object:CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
               if (currentExercisePosition < exerciseList?.size!! -1){
                   setupRestView()
               }else{
                   Toast.makeText(
                       this@ExerciseActivity,
                       "Congratulations! You have completed the workout.",
                       Toast.LENGTH_SHORT
                   ).show()
               }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress =0
        }
        binding = null
    }

}
