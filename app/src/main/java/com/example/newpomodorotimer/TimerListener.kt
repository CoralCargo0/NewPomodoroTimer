package com.example.newpomodorotimer

import android.os.CountDownTimer

interface TimerListener {
    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
    fun addNewTimer(time: Long)
    fun set(co: CountDownTimer, id: Int)

    //    fun stopOther(timer: Timer)
    fun playSong()
    fun toastNotification(text: String)
}