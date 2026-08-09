package com.example.movietiket.common.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * 영화 예매 정보(영화 + 인원 + 선택 날짜/시간 + 선택 좌석)를 표현하는 도메인 객체
 * 상태 변경 시 새로운 Reservation을 반환하는 불변 객체이다
 */
class Reservation(
    private val movie: Movie,
    private val headCount: HeadCount,
    // 극장은 "지금 예매"를 누른 뒤 예매 화면에 들어오기 전에 고르므로 항상 정해져 있다
    private val theater: Theater,
    private val selectedDate: LocalDate? = null,
    private val selectedTime: LocalTime? = null,
    private val selectedSeats: Set<String> = emptySet(),
) {
    fun increaseHeadCount(): Reservation =
        Reservation(movie, headCount.increase(), theater, selectedDate, selectedTime, selectedSeats)

    fun decreaseHeadCount(): Reservation =
        Reservation(movie, headCount.decrease(), theater, selectedDate, selectedTime, selectedSeats)

    fun selectDate(date: LocalDate): Reservation {
        require(movie.isDateAvailable(date)) { "상영 기간 내의 날짜만 선택할 수 있다: $date" }
        val times = movie.availableTimesFor(date)
        // 새 날짜에도 기존에 고른 시간이 존재하면 유지하고, 아니면 첫 시간으로 초기화한다
        val newTime = selectedTime?.takeIf { it in times } ?: times.first()
        return Reservation(movie, headCount, theater, date, newTime, selectedSeats)
    }

    fun selectTime(time: LocalTime): Reservation {
        val date = requireNotNull(selectedDate) { "날짜를 먼저 선택해야 한다" }
        require(time in movie.availableTimesFor(date)) { "선택할 수 없는 시간이다: $time" }
        return Reservation(movie, headCount, theater, date, time, selectedSeats)
    }

    fun selectSeat(seat: String): Reservation {
        require(selectedSeats.size < headCount.toInt()) {
            "선택 인원(${headCount.toDisplayValue()}명)보다 많은 좌석은 선택할 수 없다"
        }
        return Reservation(movie, headCount, theater, selectedDate, selectedTime, selectedSeats + seat)
    }

    fun deselectSeat(seat: String): Reservation =
        Reservation(movie, headCount, theater, selectedDate, selectedTime, selectedSeats - seat)

    fun isSeatSelectionComplete(): Boolean = selectedSeats.size == headCount.toInt()

    fun isSeatSelected(seat: String): Boolean = seat in selectedSeats

    fun selectedSeatsAmountWon(): Int = selectedSeats.sumOf { SeatGrade.of(it).price().toWon() }

    fun totalAmountWon(): Int = selectedSeatsAmountWon()

    fun displayMovieTitle(): String = movie.displayTitle()

    fun displaySynopsis(): String = movie.displaySynopsis()

    fun displayScreeningPeriod(): String = movie.displayScreeningPeriod()

    fun displaySelectedDate(): String = selectedDate?.toString().orEmpty()

    fun displaySelectedTime(): String = selectedTime?.toString().orEmpty()

    fun displaySelectedSeats(): String = selectedSeats.sorted().joinToString(", ")

    fun displayTheaterName(): String = theater.displayName()

    /** 상영 알림을 예약할 기준 시각 (날짜/시간이 모두 정해졌을 때만 존재한다) */
    fun screeningDateTime(): LocalDateTime? {
        val date = selectedDate ?: return null
        val time = selectedTime ?: return null
        return LocalDateTime.of(date, time)
    }

    fun runningTimeMinutes(): Int = movie.runningTimeMinutes()

    fun displayHeadCount(): String = headCount.toDisplayValue()

    fun availableDates(): List<LocalDate> = movie.availableScreeningDates()

    fun availableTimes(): List<LocalTime> = selectedDate?.let(movie::availableTimesFor).orEmpty()

    // 회전 등 구성 변경 시 상태 복원을 위한 원시값 접근자 (StateSavers에서 사용)
    fun movieId(): Int = movie.id()

    fun theaterId(): Int = theater.id()

    fun headCountValue(): Int = headCount.toInt()

    fun selectedDateValue(): LocalDate? = selectedDate

    fun selectedTimeValue(): LocalTime? = selectedTime

    fun selectedSeatsValue(): Set<String> = selectedSeats

    companion object {
        fun of(movie: Movie, theater: Theater): Reservation {
            val defaultDate = movie.defaultScreeningDate()
            val defaultTime = movie.availableTimesFor(defaultDate).first()
            return Reservation(movie, HeadCount.MINIMUM, theater, defaultDate, defaultTime)
        }
    }
}
