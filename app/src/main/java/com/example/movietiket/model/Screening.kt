package com.example.movietiket.model

/**
 * 상영 정보(상영일, 러닝타임)를 묶은 객체
 */
class Screening(
    private val date: ScreeningDate,
    private val runningTime: RunningTime,
) {
    fun displayDate(): String = date.toDisplayValue()

    fun runningTimeMinutes(): Int = runningTime.toMinutes()
}
