package com.example.newpomodorotimer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.newpomodorotimer.databinding.TimerItemBinding

class TimerAdapter(private val listener: TimerListener): ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding, listener, binding.root.context.resources)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.leftTime == newItem.leftTime &&
                        oldItem.isEnable == newItem.isEnable
            }
        }
    }
}