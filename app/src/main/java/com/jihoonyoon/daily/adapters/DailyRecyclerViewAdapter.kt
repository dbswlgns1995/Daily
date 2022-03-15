package com.jihoonyoon.daily.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jihoonyoon.daily.R
import com.jihoonyoon.daily.db.Daily

class DailyRecyclerViewAdapter constructor(private val context: Context, var items: MutableList<Daily>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val TAG = "myLog.DailyRecyclerView"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = VH(
        LayoutInflater.from(context).inflate(R.layout.item_daily, parent, false)
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val item = items[position]
        val toggleButton = vh.itemView.findViewById<AppCompatToggleButton>(R.id.item_toggleButton_daily)

        toggleButton.text = item.title
        toggleButton.textOn = item.title
        toggleButton.textOff = item.title
        toggleButton.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked){
            } else {
            }
        }
        vh.itemView.findViewById<AppCompatToggleButton>(R.id.item_toggleButton_daily).setOnLongClickListener {
            Log.d(TAG, item.title)
            true
        }
    }

    inner class VH constructor(view: View) : RecyclerView.ViewHolder(view)
}