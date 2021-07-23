package com.example.newpomodorotimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newpomodorotimer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding
    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var nextId = 0
    private var workingId = -1
    private var working: Timer? = null
    private var isPaused: Boolean = false


    var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
        .setAutoCancel(true)
        .setContentTitle("Timer")
        .setContentText("")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSilent(true)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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
        changeStopwatch(timer.id, timer.leftTime, true)
    }

    override fun stop(id: Int, currentMs: Long?) {
        changeStopwatch(id, currentMs, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }


    override fun addNewTimer(time: Long) {
        timers.add(
            Timer(
                nextId++,
                time,
                time + 1,
                false
            )
        )
        timerAdapter.submitList(timers.toList())
    }

    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    override fun onRestart() {

        isPaused = false
        super.onRestart()
    }

    override fun updateNotification(str: String) {
        if (isPaused) {
            builder
                .setContentText(str)
            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, currentMs ?: it.leftTime, it.time, isStarted))
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

    override fun toastNotification(text: String) {
        Toast.makeText(
            this@MainActivity, text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun tryiop(id: Int): Int {
        val tmp = workingId
        workingId = id
        return if (id == tmp) -1 else tmp
    }

    override fun tryiopert(timer: Timer): Timer? {

        val tmp = working
        working = timer
        return tmp
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