package com.example.newpomodorotimer

import android.os.CountDownTimer

interface TimerListener {
    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long?)
    fun delete(id: Int)
    fun addNewTimer(time: Long)
    fun playSong()
    fun toastNotification(text: String)
    fun tryiop(id: Int): Int
    fun tryiopert(timer: Timer): Timer?

    fun updateNotification(str: String)
}