package com.example.movietiket.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 영화 한 편을 표현하는 도메인 객체
 * (설명 정보와 상영 정보를 각각 묶어 인스턴스 변수를 유지한다)
 */
class Movie(
    private val id: Int,
    private val description: MovieDescription,
    private val screening: Screening,
) {
    fun id(): Int = id

    fun displayTitle(): String = description.displayTitle()

    fun displaySynopsis(): String = description.displaySynopsis()

    fun displayScreeningPeriod(): String = screening.displayPeriod()

    fun defaultScreeningDate(): LocalDate = screening.defaultDate()

    fun availableScreeningDates(): List<LocalDate> = screening.availableDates()

    fun isDateAvailable(date: LocalDate): Boolean = screening.isDateAvailable(date)

    fun availableTimesFor(date: LocalDate): List<LocalTime> = screening.availableTimesFor(date)

    fun runningTimeMinutes(): Int = screening.runningTimeMinutes()
}
