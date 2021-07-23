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
        } else totalTimerStop(timer)

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isEnable) {
                totalTimerStop(timer)
            } else {
                if (timer.leftTime <= 0) {
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
//        listener.stopOther(timer)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, null)
        setDraw(drawable)

//        getCountDownTimer(timer).let {
//            timer.timerCountDown = it
//            listener.set(it, timer.id)
//            it.start()
//        }
         val calop = listener.tryiop(timer.id)
        if (calop != -1) {
            timerr?.cancel()
            listener.stop(calop, null)
        }  ///////////////////////////////
//        val calop = listener.tryiopert(timer)
//        if (calop != null && calop.id != timer.id) totalTimerStop(calop)


        updateAnimation(timer)

        timerr?.cancel()
        timerr = getCountDownTimer(timer)
        timerr?.start()


        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }


    private fun stopTimer(timer: Timer) {
        val drawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_play_arrow_24, null
        )
        updateAnimation(timer)
        setDraw(drawable)
        timerr?.cancel()  ///timer.timerCountDown?.cancel()
        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }


    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.leftTime, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                timer.leftTime = millisUntilFinished
                binding.stopwatchTimer.text = timer.leftTime.displayTime()
                binding.customAnimation.setCurrent(millisUntilFinished)
                listener.updateNotification(millisUntilFinished.displayTime())
            }

            override fun onFinish() {
                timer.leftTime = 0L
                listener.stop(timer.id, 0L)
                listener.playSong()
                listener.toastNotification("Timer number ${timer.id + 1} is over!!!")
                stopTimer(timer)
                binding.customAnimation.setCurrent(0)
                binding.stopwatchTimer.text = timer.leftTime.displayTime()
            }
        }
    }

    private fun totalTimerStop(timer: Timer) {
        listener.stop(timer.id, timer.leftTime)
        stopTimer(timer)
    }

    private fun updateAnimation(timer: Timer) {
        binding.customAnimation.apply {
            setPeriod(timer.time)
            setCurrent(timer.leftTime)
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