package com.example.movietiket.view

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.example.movietiket.model.HeadCount
import com.example.movietiket.model.Reservation
import com.example.movietiket.navigation.Screen
import com.example.movietiket.repository.MovieRepository
import java.time.LocalDate
import java.time.LocalTime

/**
 * 회전 등 구성 변경 시에도 예매/화면 상태가 유지되도록 도메인 객체를 원시값으로 저장/복원한다
 * (Movie/Reservation은 Bundle에 직접 담을 수 없어 재조립에 필요한 값만 저장한다)
 */
private const val NO_DATE = -1L
private const val NO_TIME = -1

private fun reservationToList(reservation: Reservation): List<Any> = listOf(
    reservation.movieId(),
    reservation.headCountValue(),
    reservation.selectedDateValue()?.toEpochDay() ?: NO_DATE,
    reservation.selectedTimeValue()?.toSecondOfDay() ?: NO_TIME,
    reservation.selectedSeatsValue().toList(),
)

@Suppress("UNCHECKED_CAST")
private fun reservationFromList(saved: List<Any?>): Reservation {
    val movie = MovieRepository.findById(saved[0] as Int)
    val headCount = HeadCount(saved[1] as Int)
    val epochDay = saved[2] as Long
    val secondOfDay = saved[3] as Int
    val seats = (saved[4] as List<String>).toSet()
    return Reservation(
        movie = movie,
        headCount = headCount,
        selectedDate = epochDay.takeIf { it != NO_DATE }?.let(LocalDate::ofEpochDay),
        selectedTime = secondOfDay.takeIf { it != NO_TIME }?.let { LocalTime.ofSecondOfDay(it.toLong()) },
        selectedSeats = seats,
    )
}

val ReservationSaver: Saver<Reservation, Any> = listSaver(
    save = { reservationToList(it) },
    restore = { reservationFromList(it) },
)

private const val TAG_MOVIE_LIST = "MovieList"
private const val TAG_MOVIE_RESERVATION = "MovieReservation"
private const val TAG_SEAT_SELECTION = "SeatSelection"
private const val TAG_RESERVATION_COMPLETE = "ReservationComplete"

@Suppress("UNCHECKED_CAST")
val ScreenSaver: Saver<Screen, Any> = listSaver(
    save = { screen ->
        when (screen) {
            Screen.MovieList -> listOf(TAG_MOVIE_LIST)
            is Screen.MovieReservation -> listOf(TAG_MOVIE_RESERVATION, screen.movie.id())
            is Screen.SeatSelection -> listOf(TAG_SEAT_SELECTION, reservationToList(screen.reservation))
            is Screen.ReservationComplete -> listOf(TAG_RESERVATION_COMPLETE, reservationToList(screen.reservation))
        }
    },
    restore = { saved ->
        when (saved[0] as String) {
            TAG_MOVIE_LIST -> Screen.MovieList
            TAG_MOVIE_RESERVATION -> Screen.MovieReservation(MovieRepository.findById(saved[1] as Int))
            TAG_SEAT_SELECTION -> Screen.SeatSelection(reservationFromList(saved[1] as List<Any?>))
            TAG_RESERVATION_COMPLETE -> Screen.ReservationComplete(reservationFromList(saved[1] as List<Any?>))
            else -> Screen.MovieList
        }
    },
)
