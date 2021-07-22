package com.example.newpomodorotimer

import android.os.CountDownTimer

class Timer(
    var id: Int,
    var leftTime: Long,
    var isEnable: Boolean
) {
    var timerCountDown: CountDownTimer? = null
}