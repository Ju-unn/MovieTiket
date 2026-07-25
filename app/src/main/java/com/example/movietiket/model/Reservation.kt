package com.example.movietiket.model

/**
 * 영화 예매 정보(영화 + 인원)를 표현하는 도메인 객체
 * 인원 변경 시 새로운 Reservation을 반환하는 불변 객체이다
 */
class Reservation(
    private val movie: Movie,
    private val headCount: HeadCount,
) {
    fun increaseHeadCount(): Reservation = Reservation(movie, headCount.increase())

    fun decreaseHeadCount(): Reservation = Reservation(movie, headCount.decrease())

    fun totalAmount(): Money = headCount.totalPriceWith(TICKET_PRICE)

    fun displayMovieTitle(): String = movie.displayTitle()

    fun displaySynopsis(): String = movie.displaySynopsis()

    fun displayScreeningDate(): String = movie.displayScreeningDate()

    fun runningTimeMinutes(): Int = movie.runningTimeMinutes()

    fun displayHeadCount(): String = headCount.toDisplayValue()

    fun totalAmountWon(): Int = totalAmount().toWon()

    companion object {
        // 티켓 1장의 가격은 13,000원으로 고정한다
        private val TICKET_PRICE = Money(13_000)

        fun of(movie: Movie): Reservation = Reservation(movie, HeadCount.MINIMUM)
    }
}
