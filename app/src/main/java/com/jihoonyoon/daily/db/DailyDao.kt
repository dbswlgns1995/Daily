package com.jihoonyoon.daily.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailyDao {

    // 모든 데이터 가져오기 테스트용
    @Query("SELECT * FROM Daily")
    fun getAllDaily(): List<Daily>

    // 추가
    @Insert
    fun insertDaily(daily: Daily)

    // 해당 데이터 삭제하기
    @Query("DELETE FROM Daily where title = :title")
    fun deleteDaily(title: String)

    @Query("DELETE FROM DAILY")
    fun deleteAll()
}