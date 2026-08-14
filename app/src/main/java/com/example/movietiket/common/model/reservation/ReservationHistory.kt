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

    // 화면 표시용 영화 제목을 반환한다
    fun displayMovieTitle(): String = reservation.displayMovieTitle()

    // 화면 표시용 극장 이름을 반환한다
    fun displayTheaterName(): String = reservation.displayTheaterName()

    // 화면 표시용 상영 날짜를 반환한다
    fun displayScreeningDate(): String = reservation.displaySelectedDate()

    // 화면 표시용 상영 시간을 반환한다
    fun displayScreeningTime(): String = reservation.displaySelectedTime()
}
