package com.jihoonyoon.daily

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jihoonyoon.daily.adapters.DailyRecyclerViewAdapter
import com.jihoonyoon.daily.db.AppDatabase
import com.jihoonyoon.daily.db.Daily
import com.jihoonyoon.daily.db.DailyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*



class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "myLog.MainActivity"
    }

    // Text for showing date
    private val dateText: TextView by lazy {
        findViewById(R.id.date_textView_main)
    }

    // Add Button for showing bottomSheetView and adding daily plan
    private val addButton: FloatingActionButton by lazy {
        findViewById(R.id.add_floatingActionButton_main)
    }

    private val linearLayoutMain: LinearLayout by lazy {
        findViewById(R.id.linearlayout_main)
    }

    // Room
    private lateinit var database: AppDatabase
    private lateinit var dailyDao: DailyDao

    // BottomSheetDialog for add daily plan
    private lateinit var addBottomSheetDialog: AddBottomSheetDialogFragment

    // recyclerView
    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView_main)
    }
    private lateinit var adapter: DailyRecyclerViewAdapter


    private val dayofWeekList = listOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // room db ?????????
        initDatabase(applicationContext)

        // ?????? ?????? ??????
        showDateText()

        // add ?????? ?????? ??? ?????????
        initBottomSheet()

        initRecyclerView(applicationContext)

        // ?????? ?????? ??????
        addButton.setOnClickListener {
            addBottomSheetDialog.show(supportFragmentManager, addBottomSheetDialog.tag)
        }

        addButton.setOnLongClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dailyDao.deleteAll()
                initRecyclerView(applicationContext)
            }
            true
        }


    }

    // ?????????????????? ?????????
    public fun initRecyclerView(context: Context){

        CoroutineScope(Dispatchers.IO).launch {

            val mutableList: MutableList<Daily> = mutableListOf()
            mutableList.clear()

            val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
            dailyDao.getAllDaily().forEach {
                if (dayofWeekList[currentDayOfWeek] in it.dayOfWeek){
                    Log.d(TAG, dayofWeekList[currentDayOfWeek])
                    mutableList.add(it)
                }
            }

            var listCount = when(mutableList.count()){
                in 1..2 -> 1
                in 3..4 -> 2
                in 5..9 -> 3
                else -> 4
            }

            Log.d(TAG, listCount.toString())

            CoroutineScope(Dispatchers.Main).launch {
                adapter = DailyRecyclerViewAdapter(this@MainActivity, context, mutableList)
                val gridLayoutManager = GridLayoutManager(context, listCount, GridLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = gridLayoutManager

            }
        }

    }

    // ?????? ?????? ??????
    private fun showDateText() {
        val currentTime = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy/MM/dd/(EE)", Locale.getDefault())
        val current: String = format.format(currentTime)
        dateText.text = current
    }

    // add ?????? ?????? ??? ?????????
    private fun initBottomSheet() {
        addBottomSheetDialog = AddBottomSheetDialogFragment { save ->
            if (save) {
                // show all db
                CoroutineScope(Dispatchers.IO).launch {
                    initRecyclerView(applicationContext)
                }
            }
        }
    }

    // room db ?????????
    private fun initDatabase(context: Context) {
        database = AppDatabase.getInstance(context)!!
        dailyDao = database.dailyDao()
    }

}