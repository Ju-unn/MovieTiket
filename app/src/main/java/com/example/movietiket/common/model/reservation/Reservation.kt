package com.example.movietiket.common.model.reservation

import com.example.movietiket.common.model.DisplayDateFormat
import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.screening.SeatGrade
import com.example.movietiket.common.model.theater.Theater
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
    // 인원수를 1 증가시킨 새 예매 정보를 반환한다
    fun increaseHeadCount(): Reservation =
        Reservation(movie, headCount.increase(), theater, selectedDate, selectedTime, selectedSeats)

    // 인원수를 1 감소시킨 새 예매 정보를 반환한다
    fun decreaseHeadCount(): Reservation =
        Reservation(movie, headCount.decrease(), theater, selectedDate, selectedTime, selectedSeats)

    // 상영 날짜를 선택한 새 예매 정보를 반환한다 (가능하면 기존 시간을 유지한다)
    fun selectDate(date: LocalDate): Reservation {
        require(movie.isDateAvailable(date)) { "상영 기간 내의 날짜만 선택할 수 있다: $date" }
        val times = movie.availableTimesFor(date)
        // 새 날짜에도 기존에 고른 시간이 존재하면 유지하고, 아니면 첫 시간으로 초기화한다
        val newTime = selectedTime?.takeIf { it in times } ?: times.first()
        return Reservation(movie, headCount, theater, date, newTime, selectedSeats)
    }

    // 상영 시간을 선택한 새 예매 정보를 반환한다
    fun selectTime(time: LocalTime): Reservation {
        val date = requireNotNull(selectedDate) { "날짜를 먼저 선택해야 한다" }
        require(time in movie.availableTimesFor(date)) { "선택할 수 없는 시간이다: $time" }
        return Reservation(movie, headCount, theater, date, time, selectedSeats)
    }

    // 좌석을 선택 목록에 추가한 새 예매 정보를 반환한다
    fun selectSeat(seat: String): Reservation {
        require(selectedSeats.size < headCount.toInt()) {
            "선택 인원(${headCount.toDisplayValue()}명)보다 많은 좌석은 선택할 수 없다"
        }
        return Reservation(movie, headCount, theater, selectedDate, selectedTime, selectedSeats + seat)
    }

    // 좌석을 선택 목록에서 제거한 새 예매 정보를 반환한다
    fun deselectSeat(seat: String): Reservation =
        Reservation(movie, headCount, theater, selectedDate, selectedTime, selectedSeats - seat)

    // 선택 인원만큼 좌석을 모두 선택했는지 확인한다
    fun isSeatSelectionComplete(): Boolean = selectedSeats.size == headCount.toInt()

    // 해당 좌석이 선택되어 있는지 확인한다
    fun isSeatSelected(seat: String): Boolean = seat in selectedSeats

    // 선택한 좌석들의 총 금액(원)을 계산한다
    fun selectedSeatsAmountWon(): Int = selectedSeats.sumOf { SeatGrade.of(it).price().toWon() }

    // 예매 총 금액(원)을 반환한다
    fun totalAmountWon(): Int = selectedSeatsAmountWon()

    // 화면 표시용 영화 제목을 반환한다
    fun displayMovieTitle(): String = movie.displayTitle()

    // 화면 표시용 줄거리를 반환한다
    fun displaySynopsis(): String = movie.displaySynopsis()

    // 화면 표시용 상영 기간 문자열을 반환한다
    fun displayScreeningPeriod(): String = movie.displayScreeningPeriod()

    // 화면 표시용 선택 날짜 문자열을 반환한다
    fun displaySelectedDate(): String = selectedDate?.let(DisplayDateFormat::format).orEmpty()

    // 화면 표시용 선택 시간 문자열을 반환한다
    fun displaySelectedTime(): String = selectedTime?.let(DisplayDateFormat::format).orEmpty()

    // 화면 표시용 선택 좌석 목록 문자열을 반환한다
    fun displaySelectedSeats(): String = selectedSeats.sorted().joinToString(", ")

    // 화면 표시용 극장 이름을 반환한다
    fun displayTheaterName(): String = theater.displayName()

    /** 상영 알림을 예약할 기준 시각 (날짜/시간이 모두 정해졌을 때만 존재한다) */
    fun screeningDateTime(): LocalDateTime? {
        val date = selectedDate ?: return null
        val time = selectedTime ?: return null
        return LocalDateTime.of(date, time)
    }

    // 상영 시간(분)을 반환한다
    fun runningTimeMinutes(): Int = movie.runningTimeMinutes()

    // 화면 표시용 인원수 문자열을 반환한다
    fun displayHeadCount(): String = headCount.toDisplayValue()

    // 선택 가능한 상영 날짜 목록을 반환한다
    fun availableDates(): List<LocalDate> = movie.availableScreeningDates()

    // 선택한 날짜의 선택 가능한 상영 시간 목록을 반환한다
    fun availableTimes(): List<LocalTime> = selectedDate?.let(movie::availableTimesFor).orEmpty()

    // 회전 등 구성 변경 시 상태 복원을 위한 원시값 접근자 (StateSavers에서 사용)
    fun movieId(): Int = movie.id()

    fun theaterId(): Int = theater.id()

    fun headCountValue(): Int = headCount.toInt()

    fun selectedDateValue(): LocalDate? = selectedDate

    fun selectedTimeValue(): LocalTime? = selectedTime

    fun selectedSeatsValue(): Set<String> = selectedSeats

    companion object {
        // 영화와 극장으로 기본값(최소 인원, 상영 시작일의 첫 시간)이 설정된 예매 정보를 생성한다
        fun of(movie: Movie, theater: Theater): Reservation {
            val defaultDate = movie.defaultScreeningDate()
            val defaultTime = movie.availableTimesFor(defaultDate).first()
            return Reservation(movie, HeadCount.MINIMUM, theater, defaultDate, defaultTime)
        }
    }
}
