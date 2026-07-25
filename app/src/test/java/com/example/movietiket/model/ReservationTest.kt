package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ReservationTest {

    private fun movie(): Movie {
        return Movie(
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningDate("2024.3.1"), RunningTime(152)),
        )
    }

    @Test
    @DisplayName("예매는 최소 인원 1명으로 시작한다")
    fun startsWithMinimumHeadCount() {
        val reservation = Reservation.of(movie())

        assertThat(reservation.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("인원을 늘리면 인원이 증가한 새 예매를 반환한다")
    fun increaseHeadCount() {
        val reservation = Reservation.of(movie())

        val increased = reservation.increaseHeadCount()

        assertThat(increased.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 1명일 때 줄여도 1명이 유지된다")
    fun decreaseKeepsMinimum() {
        val reservation = Reservation.of(movie())

        val decreased = reservation.decreaseHeadCount()

        assertThat(decreased.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("총 결제 금액은 인원 수 x 13,000원이다")
    fun totalAmount() {
        val reservation = Reservation.of(movie())
            .increaseHeadCount()

        assertThat(reservation.totalAmount()).isEqualTo(Money(26_000))
        assertThat(reservation.totalAmountWon()).isEqualTo(26_000)
    }

    @Test
    @DisplayName("예매에서 영화 정보를 표시용 값으로 조회할 수 있다")
    fun displayMovieInformation() {
        val reservation = Reservation.of(movie())

        assertThat(reservation.displayMovieTitle()).isEqualTo("해리 포터와 마법사의 돌")
        assertThat(reservation.displayScreeningDate()).isEqualTo("2024.3.1")
        assertThat(reservation.runningTimeMinutes()).isEqualTo(152)
    }
}
