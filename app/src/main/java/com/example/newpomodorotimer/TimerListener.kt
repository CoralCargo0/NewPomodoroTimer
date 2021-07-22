package com.example.newpomodorotimer

interface TimerListener {
    fun start(timer: Timer)
    fun stop(id: Int, currentMs: Long)
    fun delete(id: Int)
    fun qwerty(hour: Int, minute: Int)
    fun addNewTimer()

    //    fun stopOther(timer: Timer)
    fun playSong()
    fun toastNotification(text: String)
}