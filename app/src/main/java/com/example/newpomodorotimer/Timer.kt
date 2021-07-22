package com.example.newpomodorotimer

import android.os.CountDownTimer

data class Timer(
    val id: Int,
    var leftTime: Long,
    var isEnable: Boolean,
//    val timerCountDown: CountDownTimer
)