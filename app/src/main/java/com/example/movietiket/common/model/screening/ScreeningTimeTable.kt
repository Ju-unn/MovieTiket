package com.example.movietiket.common.model.screening

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * 날짜별 상영 시간을 계산한다
 * 평일은 오전 10시, 주말은 오전 9시부터 2시간 간격으로 자정 전까지 상영한다
 */
object ScreeningTimeTable {
    private const val WEEKDAY_START_HOUR = 10
    private const val WEEKEND_START_HOUR = 9
    private const val INTERVAL_HOURS = 2
    private const val END_HOUR_EXCLUSIVE = 24

    fun timesFor(date: LocalDate): List<LocalTime> {
        val startHour = if (isWeekend(date)) WEEKEND_START_HOUR else WEEKDAY_START_HOUR
        return (startHour until END_HOUR_EXCLUSIVE step INTERVAL_HOURS).map { LocalTime.of(it, 0) }
    }

    private fun isWeekend(date: LocalDate): Boolean =
        date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
}
