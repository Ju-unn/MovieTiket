package com.example.movietiket.common.model.screening

import com.example.movietiket.common.model.movie.RunningTime
import java.time.LocalDate
import java.time.LocalTime

/**
 * 상영 정보(상영 기간, 러닝타임)를 묶은 객체
 */
class Screening(
    private val period: ScreeningPeriod,
    private val runningTime: RunningTime,
) {
    // 화면 표시용 상영 기간 문자열을 반환한다
    fun displayPeriod(): String = period.displayValue()

    // 상영 시작일(기본 날짜)을 반환한다
    fun defaultDate(): LocalDate = period.startDate()

    // 선택 가능한 상영 날짜 목록을 반환한다
    fun availableDates(): List<LocalDate> = period.dates()

    // 해당 날짜가 상영 기간에 포함되는지 확인한다
    fun isDateAvailable(date: LocalDate): Boolean = period.contains(date)

    // 해당 날짜의 상영 가능 시간 목록을 반환한다
    fun availableTimesFor(date: LocalDate): List<LocalTime> {
        require(isDateAvailable(date)) { "상영 기간이 아닌 날짜다: $date" }
        return ScreeningTimeTable.timesFor(date)
    }

    // 러닝타임(분)을 반환한다
    fun runningTimeMinutes(): Int = runningTime.toMinutes()
}
