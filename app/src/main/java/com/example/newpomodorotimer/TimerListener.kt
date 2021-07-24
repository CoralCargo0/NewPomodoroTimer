package com.example.newpomodorotimer

interface TimerListener {
    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long?)
    fun delete(id: Int)
    fun addNewTimer(time: Long)
    fun playSong()
    fun toastNotification(text: String)
    fun whatTimerIsEnable(timer: Timer): Timer?
    fun vibration()
}