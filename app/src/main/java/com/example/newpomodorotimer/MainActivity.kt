package com.example.newpomodorotimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newpomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding
    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var nextId = 0
    private var time = 0L

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
        changeStopwatch(timer.id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false)
        timers.find {
            it.id == id
        }?.timerCountDown?.cancel()
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    override fun qwerty(hour: Int, minute: Int) {
        time = ((hour * 60 + minute) * 60000).toLong()
    }

    override fun addNewTimer() {
        timers.add(Timer(nextId++, time, false))
        timerAdapter.submitList(timers.toList())
    }

    override fun set(co: CountDownTimer, id: Int) {
        timers.find {
            it.id == id
        }?.timerCountDown = co
    }

    override fun playSong() {
        val song: MediaPlayer = MediaPlayer.create(this, R.raw.message)
        song.start()
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, currentMs ?: it.leftTime, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

    override fun toastNotification(text: String) {
        Toast.makeText(
            this@MainActivity, text,
            Toast.LENGTH_SHORT
        ).show()
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