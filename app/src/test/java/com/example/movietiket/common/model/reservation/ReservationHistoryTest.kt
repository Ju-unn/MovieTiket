package com.example.movietiket.common.model.reservation

import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * ReservationHistory의 id/예매 정보 조회 및 표시용 값 위임을 검증하는 테스트
 */
class ReservationHistoryTest {

    // id와 예매 정보가 그대로 조회되는지 검증
    @Test
    @DisplayName("id와 예매 정보를 그대로 조회할 수 있다")
    fun idAndReservation() {
        val reservation = Reservation.of(testMovie(), testTheater())
        val history = ReservationHistory(id = 1L, reservation = reservation)

        assertThat(history.id()).isEqualTo(1L)
        assertThat(history.reservation()).isEqualTo(reservation)
    }

    // 표시용 값 조회가 내부 예매 정보에 그대로 위임되는지 검증
    @Test
    @DisplayName("예매 정보의 표시용 값들을 그대로 위임해 조회한다")
    fun displayDelegatesToReservation() {
        val reservation = Reservation.of(testMovie(), testTheater())
        val history = ReservationHistory(id = 1L, reservation = reservation)

        assertThat(history.displayMovieTitle()).isEqualTo(reservation.displayMovieTitle())
        assertThat(history.displayTheaterName()).isEqualTo(reservation.displayTheaterName())
        assertThat(history.displayScreeningDate()).isEqualTo(reservation.displaySelectedDate())
        assertThat(history.displayScreeningTime()).isEqualTo(reservation.displaySelectedTime())
    }
}
