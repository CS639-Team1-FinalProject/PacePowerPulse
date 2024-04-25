package com.example.pacepowerpulse

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacepowerpulse.databinding.ActivityExerciseBinding
import com.example.pacepowerpulse.databinding.DialogueCustomBackConfirmationBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
        private var binding: ActivityExerciseBinding?=null

        private var restTimer: CountDownTimer? =null
        private var restProgress = 0
    private var restTimerDuration:Long = 1
    private var exerciseTimeDuration:Long=1
        private var exerciseTimer: CountDownTimer? =null
        private var exerciseProgress = 0

        private var exerciseList : ArrayList<ExerciseModel>?= null
        private var currentExercisePosition = -1

        private var tts: TextToSpeech? = null // Variable for Text to Speech
        // END
        private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


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

            tts = TextToSpeech(this, this)


            binding?.exerciseToolbar?.setNavigationOnClickListener {
                customDialogForBackButton()
            }
            setupRestView()
//            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//                insets
//            }
        setupExerciseStatusRecyclerView()
        }

    private fun setupRestView(){
        try {
            val soundURI =
                Uri.parse("android.resource://com.example.pacepowerpulse/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false // Sets the player to be looping or non-looping.
            player?.start() // Starts Playback.
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
      binding?.progressbar?.progress = restProgress

      restTimer = object:CountDownTimer(restTimerDuration*10000,1000){
          override fun onTick(millisUntilFinished: Long) {
              restProgress++
              binding?.progressbar?.progress = 10 - restProgress
              binding?.tvTimer?.text = (10 - restProgress).toString()
          }

          override fun onFinish() {
              currentExercisePosition++
              exerciseList!![currentExercisePosition].setIsSelected(true) // Current Item is selected
              exerciseAdapter?.notifyDataSetChanged() // Notified the current item to adapter class to reflect it into UI.
              setupExerciseView()
          }

      }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object:CountDownTimer(exerciseTimeDuration*30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {


               if (currentExercisePosition < exerciseList?.size!! -1){
                   exerciseList!![currentExercisePosition].setIsSelected(false) // exercise is completed so selection is set to false
                   exerciseList!![currentExercisePosition].setIsCompleted(true) // updating in the list that this exercise is completed
                   exerciseAdapter?.notifyDataSetChanged()
                   setupRestView()
               }else{
                   finish()
                   val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                   startActivity(intent)
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
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player != null){
            player!!.stop()
        }
        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {

        // TODO (Step 5 - After variable initializing set the language after a "success"ful result.)
        // START
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
        // END
    }
    // END
    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
    private fun setupExerciseStatusRecyclerView() {

        //layout manager for the recycle view
        // LinearLayout Manager for horizontal scroll.
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)

        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }
//override fun onBackPressed(){
//    customDialogForBackButton()
//    //super.onBackPressed()
//}

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogueCustomBackConfirmationBinding.inflate(layoutInflater)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        // bind to the dialog
        customDialog.setContentView(dialogBinding.root)
        //to ensure that the user clicks one of the button and that the dialog is
        //not dismissed when surrounding parts of the screen is clicked
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            // We need to specify that we are finishing this activity if not the player
            // continues beeping even after the screen is not visibile
            this@ExerciseActivity.finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }
}
