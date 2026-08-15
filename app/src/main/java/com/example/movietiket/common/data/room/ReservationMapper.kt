package com.example.movietiket.common.data.room

import com.example.movietiket.common.model.reservation.HeadCount
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import java.time.LocalDate
import java.time.LocalTime

private const val SEAT_DELIMITER = ","

// Reservation을 저장용 Entity로 변환한다 (영화/극장은 id만 저장)
fun Reservation.toEntity(reservedAt: Long): ReservationEntity = ReservationEntity(
    movieId = movieId(),
    theaterId = theaterId(),
    headCount = headCountValue(),
    screeningDate = requireNotNull(selectedDateValue()) { "상영 날짜가 정해진 예매만 저장할 수 있다" }.toEpochDay(),
    screeningTime = requireNotNull(selectedTimeValue()) { "상영 시각이 정해진 예매만 저장할 수 있다" }.toSecondOfDay(),
    seats = selectedSeatsValue().sorted().joinToString(SEAT_DELIMITER),
    reservedAt = reservedAt,
)

// 저장된 Entity를 도메인 예매 내역(ReservationHistory)으로 재조립한다
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
