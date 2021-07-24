package com.example.newpomodorotimer

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var timePasser: TimerListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        timePasser = context as TimerListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = 0
        val minute = 5
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        timePasser.addNewTimer(((hourOfDay * 60 + minute) * 60000).toLong())
    }
}