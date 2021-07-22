package com.example.newpomodorotimer

import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newpomodorotimer.databinding.TimerItemBinding

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var timerr: CountDownTimer? = null

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = timer.leftTime.displayTime()

        if (timer.isEnable) {
            startTimer(timer)
        } else stopTimer(timer)

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isEnable) {
                listener.stop(timer.id, timer.leftTime)
            } else {
                listener.start(timer)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
    }

    private fun startTimer(timer: Timer) {
//        listener.stopOther(timer)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, null)
        setDraw(drawable)

        timerr?.cancel()
        timerr = getCountDownTimer(timer)
        timerr?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun setDraw(drawable: Drawable?) {
        binding.apply {
            startPauseButton.setImageDrawable(drawable)
            startPauseButton.setColorFilter(
                R.color.black, PorterDuff.Mode.SRC_IN
            )
            deleteButton.setColorFilter(
                R.color.black, PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun stopTimer(timer: Timer) {
        val drawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_play_arrow_24, null
        )

        setDraw(drawable)

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 3600000
        val m = (this / 60000) % 60
        val s = this / 1000 % 60
        // val ms = this % 1000 / 10

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_S) {
            val interval = UNIT_S

            override fun onTick(millisUntilFinished: Long) {
                timer.leftTime -= interval
                if (timer.leftTime <= 0L) {
                    listener.playSong()
                    stopTimer(timer)
                }
                binding.stopwatchTimer.text = timer.leftTime.displayTime()
            }
            override fun onFinish() {
                binding.stopwatchTimer.text = timer.leftTime.displayTime()
            }
        }
    }


    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_S = 10L
        private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }

}