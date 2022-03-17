package com.jihoonyoon.daily.adapters

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jihoonyoon.daily.MainActivity
import com.jihoonyoon.daily.R
import com.jihoonyoon.daily.db.AppDatabase
import com.jihoonyoon.daily.db.Daily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyRecyclerViewAdapter constructor(private val activity: MainActivity,private val context: Context, var items: MutableList<Daily>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val database: AppDatabase by lazy { AppDatabase.getInstance(context)!! }
    private val dailyDao by lazy { database.dailyDao() }


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

        var listCount = when(items.count()){
            in 1..2 -> 1
            in 3..4 -> 2
            in 5..9 -> 3
            else -> 4
        }

        // 크기 조절
        val size = Point()
        activity.windowManager.defaultDisplay.getRealSize(size)
        vh.itemView.layoutParams = ViewGroup.LayoutParams(size.x /listCount ,size.y /listCount)

        // toggle Button Setting
        toggleButton.text = item.title
        toggleButton.textOn = item.title
        toggleButton.textOff = item.title
        toggleButton.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked){

            } else {

            }
        }

        // 아이템 long 클릭 시 해당 아이템 삭제 및 refresh
        vh.itemView.findViewById<AppCompatToggleButton>(R.id.item_toggleButton_daily).setOnLongClickListener {
            Log.d(TAG, item.title)
            CoroutineScope(Dispatchers.IO).launch {
                dailyDao.deleteDaily(item.title)
                CoroutineScope(Dispatchers.Main).launch {
                    activity.initRecyclerView(activity)
                }
            }
            true
        }
    }

    inner class VH constructor(view: View) : RecyclerView.ViewHolder(view)
}