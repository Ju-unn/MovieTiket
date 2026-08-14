package com.example.movietiket.common.data

import com.example.movietiket.common.model.reservation.HeadCount
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import java.time.LocalDate
import java.time.LocalTime

private const val SEAT_DELIMITER = ","

/**
 * 도메인 <-> 테이블 변환
 * 영화/극장은 고정 데이터이므로 id로 다시 조회해 도메인 객체를 재조립한다
 */
fun Reservation.toEntity(reservedAt: Long): ReservationEntity = ReservationEntity(
    movieId = movieId(),
    theaterId = theaterId(),
    headCount = headCountValue(),
    screeningDate = requireNotNull(selectedDateValue()) { "상영 날짜가 정해진 예매만 저장할 수 있다" }.toEpochDay(),
    screeningTime = requireNotNull(selectedTimeValue()) { "상영 시각이 정해진 예매만 저장할 수 있다" }.toSecondOfDay(),
    seats = selectedSeatsValue().sorted().joinToString(SEAT_DELIMITER),
    reservedAt = reservedAt,
)

fun ReservationEntity.toReservationHistory(): ReservationHistory = ReservationHistory(
    id = id,
    reservation = Reservation(
        movie = MovieRepository.findById(movieId),
        headCount = HeadCount(headCount),
        theater = TheaterRepository.findById(theaterId),
        selectedDate = LocalDate.ofEpochDay(screeningDate),
        selectedTime = LocalTime.ofSecondOfDay(screeningTime.toLong()),
        selectedSeats = seats.split(SEAT_DELIMITER).filter { it.isNotBlank() }.toSet(),
    ),
)
