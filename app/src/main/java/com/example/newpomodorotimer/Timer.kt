package com.example.newpomodorotimer

import android.os.CountDownTimer

data class Timer(
    val id: Int,
    var remainingTime: Long,
    val time: Long,
    var isEnable: Boolean,
    var timerCountDown: CountDownTimer?
)