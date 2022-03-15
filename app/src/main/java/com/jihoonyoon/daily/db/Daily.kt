package com.jihoonyoon.daily.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.gson.Gson

@Entity class Daily(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "dayOfWeek") var dayOfWeek: List<String>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}