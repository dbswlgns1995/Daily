package com.jihoonyoon.daily

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jihoonyoon.daily.db.AppDatabase
import com.jihoonyoon.daily.db.Daily
import com.jihoonyoon.daily.db.DailyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBottomSheetDialogFragment (val save: (Boolean) -> Unit): BottomSheetDialogFragment() {

    companion object {
        const val TAG = "myLog.AddBottomSheetDialogFragment"
    }

    // Room
    private lateinit var database: AppDatabase
    private lateinit var dailyDao: DailyDao

    // day Picker
    private lateinit var dayPicker: MaterialDayPicker
    private lateinit var editText: AppCompatEditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button

    override fun onStart() {
        super.onStart()

        // 바텀 시트 화면의 0.8 크기 만큼 출력
        if (dialog != null) {
            val bottomSheet: View = dialog!!.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val view = view
        view!!.post{
            val parent = view!!.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = (view!!.measuredHeight * 0.8).toInt()
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.dialog_add_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewBinding(view)
        initDatabase(view)

        cancelButton.setOnClickListener { dismiss() }
        saveButton.setOnClickListener {
            val dateList :MutableList<String> = mutableListOf()
            dateList.clear()

            // title, [dayOfWeek] Database 에 저장하기
            if(editText.text?.isEmpty() == true){
                Toast.makeText(view.context, "제목 입력!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(dayPicker.selectedDays.count() == 0){
                Toast.makeText(view.context, "요일 선택!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // MutableList 에 넣기
            dayPicker.selectedDays.forEach {
                Log.d(TAG, it.name)
                dateList.add(it.name)
            }

            // save
            CoroutineScope(Dispatchers.IO).launch {
                dailyDao.insertDaily(Daily(editText.text.toString(), dateList.toList()))
                dismiss()
                save(true)
            }


        }
    }

    // initViewBinding
    private fun initViewBinding(view: View){
        dayPicker = view.findViewById(R.id.day_picker_bottom_sheet)
        editText = view.findViewById(R.id.title_edit_bottom_sheet)
        cancelButton = view.findViewById(R.id.cancel_button_bottom_sheet)
        saveButton = view.findViewById(R.id.save_button_bottom_sheet)
    }

    private fun initDatabase(view: View){
        database = AppDatabase.getInstance(view.context)!!
        dailyDao = database.dailyDao()
    }
}