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

    // 상영 기간에 포함된 모든 날짜 목록을 생성한다
    fun dates(): List<LocalDate> = generateSequence(startDate) { it.plusDays(1) }
        .takeWhile { !it.isAfter(endDate) }
        .toList()

    // 해당 날짜가 상영 기간에 포함되는지 확인한다
    fun contains(date: LocalDate): Boolean = !date.isBefore(startDate) && !date.isAfter(endDate)

    // 화면 표시용 상영 기간 문자열을 반환한다
    fun displayValue(): String =
        "${DisplayDateFormat.format(startDate)} ~ ${DisplayDateFormat.format(endDate)}"
}
