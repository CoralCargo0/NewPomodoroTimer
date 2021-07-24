package com.example.newpomodorotimer

import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newpomodorotimer.databinding.TimerItemBinding
import com.google.android.material.color.MaterialColors.getColor

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = timer.remainingTime.displayTime()
        timer.timerCountDown = getCountDownTimer(timer)
        if (timer.isEnable) {
            startTimer(timer)
        } else stopTimer(timer)

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isEnable) {
                totalTimerStop(timer)
            } else {
                if (timer.remainingTime <= 0) {
                    listener.toastNotification("This timer is over!")
                    listener.delete(timer.id)
                } else {
                    listener.start(timer)
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            totalTimerStop(timer)
            listener.delete(timer.id)
        }
    }

    private fun startTimer(timer: Timer) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, null)
        setDraw(drawable)

        val enabledTimerToStop = listener.whatTimerIsEnable(timer)
        if (enabledTimerToStop != null) {
            enabledTimerToStop.timerCountDown?.cancel()
            listener.stop(enabledTimerToStop.id, enabledTimerToStop.remainingTime)
        }

        updateAnimation(timer)
        timer.timerCountDown?.start()
        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }


    private fun stopTimer(timer: Timer) {
        timer.timerCountDown?.cancel()

        val drawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_play_arrow_24, null
        )
        setDraw(drawable)
        updateAnimation(timer)
        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }


    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.remainingTime, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                timer.remainingTime = millisUntilFinished
                binding.stopwatchTimer.text = timer.remainingTime.displayTime()
                binding.customAnimation.setCurrent(millisUntilFinished)
            }

            override fun onFinish() {
                timer.remainingTime = 0L
                totalTimerStop(timer)
                listener.playSong()
                listener.toastNotification("Your timer is over!!!")
                listener.vibration()
                binding.customAnimation.setCurrent(timer.remainingTime)
                binding.stopwatchTimer.text = timer.remainingTime.displayTime()
            }
        }
    }

    private fun totalTimerStop(timer: Timer) {
        listener.stop(timer.id, timer.remainingTime)
        stopTimer(timer)
    }

    private fun updateAnimation(timer: Timer) {
        binding.customAnimation.apply {
            setPeriod(timer.time)
            setCurrent(timer.remainingTime)
        }
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
}