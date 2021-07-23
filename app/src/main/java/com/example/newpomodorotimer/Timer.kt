package com.example.newpomodorotimer

import android.os.CountDownTimer

data class Timer(
    val id: Int,
    var leftTime: Long,
    val time: Long,
    var isEnable: Boolean,
//    val timerCountDown: CountDownTimer
)