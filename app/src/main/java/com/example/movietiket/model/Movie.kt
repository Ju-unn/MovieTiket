package com.example.movietiket.model

/**
 * 영화 한 편을 표현하는 도메인 객체
 * (설명 정보와 상영 정보를 각각 묶어 인스턴스 변수를 2개로 유지한다)
 */
class Movie(
    private val description: MovieDescription,
    private val screening: Screening,
) {
    fun displayTitle(): String = description.displayTitle()

    fun displaySynopsis(): String = description.displaySynopsis()

    fun displayScreeningDate(): String = screening.displayDate()

    fun runningTimeMinutes(): Int = screening.runningTimeMinutes()
}
