package com.example.movietiket.common.model.screening

import com.example.movietiket.common.model.DisplayDateFormat
import java.time.LocalDate

/**
 * 상영 기간(시작일~종료일)을 표현하는 값 객체
 */
class ScreeningPeriod(
    private val startDate: LocalDate,
    private val endDate: LocalDate,
) {
    init {
        require(!endDate.isBefore(startDate)) { "종료일은 시작일보다 앞설 수 없다: $startDate ~ $endDate" }
    }

    fun startDate(): LocalDate = startDate

    fun dates(): List<LocalDate> = generateSequence(startDate) { it.plusDays(1) }
        .takeWhile { !it.isAfter(endDate) }
        .toList()

    fun contains(date: LocalDate): Boolean = !date.isBefore(startDate) && !date.isAfter(endDate)

    fun displayValue(): String =
        "${DisplayDateFormat.format(startDate)} ~ ${DisplayDateFormat.format(endDate)}"
}
