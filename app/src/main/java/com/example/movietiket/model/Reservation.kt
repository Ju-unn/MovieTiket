package com.example.movietiket.model

/**
 * 영화 예매 정보(영화 + 인원 + 선택 시간 + 선택 좌석)를 표현하는 도메인 객체
 * 상태 변경 시 새로운 Reservation을 반환하는 불변 객체이다
 */
class Reservation(
    private val movie: Movie,
    private val headCount: HeadCount,
    private val selectedTime: String? = null,
    private val selectedSeats: Set<String> = emptySet(),
) {
    fun increaseHeadCount(): Reservation = Reservation(movie, headCount.increase(), selectedTime, selectedSeats)

    fun decreaseHeadCount(): Reservation = Reservation(movie, headCount.decrease(), selectedTime, selectedSeats)

    fun selectTime(time: String): Reservation = Reservation(movie, headCount, time, selectedSeats)

    fun selectSeat(seat: String): Reservation {
        require(selectedSeats.size < headCount.toInt()) {
            "선택 인원(${headCount.toDisplayValue()}명)보다 많은 좌석은 선택할 수 없다"
        }
        return Reservation(movie, headCount, selectedTime, selectedSeats + seat)
    }

    fun deselectSeat(seat: String): Reservation = Reservation(movie, headCount, selectedTime, selectedSeats - seat)

    fun isSeatSelectionComplete(): Boolean = selectedSeats.size == headCount.toInt()

    fun isSeatSelected(seat: String): Boolean = seat in selectedSeats

    fun selectedSeatsAmountWon(): Int = (TICKET_PRICE * selectedSeats.size).toWon()

    fun totalAmount(): Money = headCount.totalPriceWith(TICKET_PRICE)

    fun displayMovieTitle(): String = movie.displayTitle()

    fun displaySynopsis(): String = movie.displaySynopsis()

    fun displayScreeningDate(): String = movie.displayScreeningDate()

    fun displaySelectedTime(): String = selectedTime.orEmpty()

    fun displaySelectedSeats(): String = selectedSeats.sorted().joinToString(", ")

    fun runningTimeMinutes(): Int = movie.runningTimeMinutes()

    fun displayHeadCount(): String = headCount.toDisplayValue()

    fun totalAmountWon(): Int = totalAmount().toWon()

    fun ticketPriceWon(): Int = TICKET_PRICE.toWon()

    companion object {
        // 티켓 1장의 가격은 13,000원으로 고정한다
        private val TICKET_PRICE = Money(13_000)

        fun of(movie: Movie): Reservation = Reservation(movie, HeadCount.MINIMUM)
    }
}
