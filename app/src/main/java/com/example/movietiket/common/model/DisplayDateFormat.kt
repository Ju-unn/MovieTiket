package com.example.movietiket.common.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * 화면에 보여줄 날짜/시각 표기
 * 디자인이 "2024.3.2 17:00"처럼 점으로 잇고 0을 붙이지 않으므로 여기서 한 번에 맞춘다
 */
object DisplayDateFormat {

    private val DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d")
    private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun format(date: LocalDate): String = date.format(DATE)

    fun format(time: LocalTime): String = time.format(TIME)
}
