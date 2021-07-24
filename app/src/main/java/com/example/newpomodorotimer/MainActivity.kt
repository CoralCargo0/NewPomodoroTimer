package com.example.newpomodorotimer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newpomodorotimer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding
    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var nextId = 0
    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            recycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = timerAdapter
            }

            addNewStopwatchButton.setOnClickListener {
                TimePickerFragment().show(supportFragmentManager, "")
            }
        }
    }

    override fun start(timer: Timer) {
        changeStopwatch(timer.id, timer.remainingTime, true)
    }

    override fun stop(id: Int, currentMs: Long?) {
        changeStopwatch(id, currentMs, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
        if (timers.size < 11) binding.addNewStopwatchButton.isEnabled = true
    }


    override fun addNewTimer(time: Long) {
        timers.add(
            Timer(
                nextId++,
                time,
                time + 1,
                false,
                null
            )
        )
        timerAdapter.submitList(timers.toList())
        if (timers.size >= 11) binding.addNewStopwatchButton.isEnabled = false
    }

    override fun onRestart() {

        isPaused = false
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
        startTimerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimerService()
    }

    override fun onStart() {
        super.onStart()
        stopTimerService()
    }

    private fun startTimerService() {
        if (timers.find { it.isEnable } != null) {
            val startIntent = Intent(this, ForegroundService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(STARTED_TIMER_TIME_MS, timers.find { it.isEnable }?.remainingTime)
            startService(startIntent)
        }
    }

    private fun stopTimerService() {
            val stopIntent = Intent(this, ForegroundService::class.java)
            stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
            startService(stopIntent)
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(
                    Timer(
                        it.id,
                        currentMs ?: it.remainingTime,
                        it.time,
                        isStarted,
                        it.timerCountDown
                    )
                )
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

    override fun playSong() {
        val song: MediaPlayer = MediaPlayer.create(this, R.raw.message)
        song.start()
    }

    override fun vibration() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        val milliseconds = 2500L

        if (canVibrate) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    override fun toastNotification(text: String) {
        Toast.makeText(
            this@MainActivity, text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun whatTimerIsEnable(timer: Timer): Timer? {
        return timers.find{it.isEnable && timer != it}
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this@MainActivity).apply {
            setTitle("Confirmation")
            setMessage("Are you sure you want to exit the app?")

            setPositiveButton("Yes") { _, _ ->
                toastNotification("Bye-bye")
                super.onBackPressed()
            }

            setNegativeButton("No") { _, _ ->
                toastNotification("Thank you")
            }
        }.show()
    }

}