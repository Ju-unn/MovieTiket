package com.example.movietiket.common.model.reservation

/**
 * 로컬 DB에 저장된 예매 한 건
 * 예매 내역 목록에서 항목을 구분하고, 상세 화면으로 넘길 때 id를 사용한다
 */
class ReservationHistory(
    private val id: Long,
    private val reservation: Reservation,
) {
    fun id(): Long = id

    fun reservation(): Reservation = reservation

    fun displayMovieTitle(): String = reservation.displayMovieTitle()

    fun displayTheaterName(): String = reservation.displayTheaterName()

    fun displayScreeningDate(): String = reservation.displaySelectedDate()

    fun displayScreeningTime(): String = reservation.displaySelectedTime()
}
