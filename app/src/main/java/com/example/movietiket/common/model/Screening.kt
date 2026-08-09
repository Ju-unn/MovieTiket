package com.example.movietiket.common.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 상영 정보(상영 기간, 러닝타임)를 묶은 객체
 */
class Screening(
    private val period: ScreeningPeriod,
    private val runningTime: RunningTime,
) {
    fun displayPeriod(): String = period.displayValue()

    fun defaultDate(): LocalDate = period.startDate()

    fun availableDates(): List<LocalDate> = period.dates()

    fun isDateAvailable(date: LocalDate): Boolean = period.contains(date)

    fun availableTimesFor(date: LocalDate): List<LocalTime> {
        require(isDateAvailable(date)) { "상영 기간이 아닌 날짜다: $date" }
        return ScreeningTimeTable.timesFor(date)
    }

    fun runningTimeMinutes(): Int = runningTime.toMinutes()
}
