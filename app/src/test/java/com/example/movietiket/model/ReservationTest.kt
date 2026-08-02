package com.example.movietiket.model

import com.example.movietiket.fixture.testMovie
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ReservationTest {

    @Test
    @DisplayName("예매는 최소 인원 1명으로 시작한다")
    fun startsWithMinimumHeadCount() {
        val reservation = Reservation.of(testMovie())

        assertThat(reservation.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("인원을 늘리면 인원이 증가한 새 예매를 반환한다")
    fun increaseHeadCount() {
        val reservation = Reservation.of(testMovie())

        val increased = reservation.increaseHeadCount()

        assertThat(increased.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 1명일 때 줄여도 1명이 유지된다")
    fun decreaseKeepsMinimum() {
        val reservation = Reservation.of(testMovie())

        val decreased = reservation.decreaseHeadCount()

        assertThat(decreased.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("총 결제 금액은 인원 수 x 13,000원이다")
    fun totalAmount() {
        val reservation = Reservation.of(testMovie())
            .increaseHeadCount()

        assertThat(reservation.totalAmount()).isEqualTo(Money(26_000))
    }

    @Test
    @DisplayName("총 결제 금액을 원 단위 숫자로 조회할 수 있다")
    fun totalAmountWon() {
        val reservation = Reservation.of(testMovie())
            .increaseHeadCount()

        assertThat(reservation.totalAmountWon()).isEqualTo(26_000)
    }

    @Test
    @DisplayName("예매에서 영화 제목을 표시용 값으로 조회할 수 있다")
    fun displayMovieTitle() {
        val reservation = Reservation.of(testMovie())

        assertThat(reservation.displayMovieTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("예매에서 영화 소개를 표시용 값으로 조회할 수 있다")
    fun displaySynopsis() {
        val reservation = Reservation.of(testMovie())

        assertThat(reservation.displaySynopsis()).isEqualTo("소개")
    }

    @Test
    @DisplayName("예매에서 상영일을 표시용 값으로 조회할 수 있다")
    fun displayScreeningDate() {
        val reservation = Reservation.of(testMovie())

        assertThat(reservation.displayScreeningDate()).isEqualTo("2024.3.1")
    }

    @Test
    @DisplayName("예매에서 러닝타임을 분 단위로 조회할 수 있다")
    fun runningTimeMinutes() {
        val reservation = Reservation.of(testMovie())

        assertThat(reservation.runningTimeMinutes()).isEqualTo(152)
    }

    @Test
    @DisplayName("선택한 시간을 표시용 값으로 조회할 수 있다")
    fun selectTime() {
        val reservation = Reservation.of(testMovie()).selectTime("13:00")

        assertThat(reservation.displaySelectedTime()).isEqualTo("13:00")
    }

    @Test
    @DisplayName("인원수만큼 좌석을 선택하면 좌석 선택이 완료된다")
    fun selectSeatsUpToHeadCount() {
        val reservation = Reservation.of(testMovie())
            .increaseHeadCount() // 2명
            .selectSeat("A1")
            .selectSeat("A2")

        assertThat(reservation.displaySelectedSeats()).isEqualTo("A1, A2")
        assertThat(reservation.isSeatSelectionComplete()).isTrue()
    }

    @Test
    @DisplayName("인원수보다 많은 좌석은 선택할 수 없다")
    fun cannotSelectMoreSeatsThanHeadCount() {
        val reservation = Reservation.of(testMovie()).selectSeat("A1") // 1명, 1석 선택 완료

        assertThatIllegalArgumentException()
            .isThrownBy { reservation.selectSeat("A2") }
    }

    @Test
    @DisplayName("좌석 선택을 취소할 수 있다")
    fun deselectSeat() {
        val reservation = Reservation.of(testMovie()).selectSeat("A1").deselectSeat("A1")

        assertThat(reservation.displaySelectedSeats()).isEmpty()
        assertThat(reservation.isSeatSelectionComplete()).isFalse()
    }

    @Test
    @DisplayName("좌석 선택 여부를 조회할 수 있다")
    fun isSeatSelected() {
        val reservation = Reservation.of(testMovie()).selectSeat("A1")

        assertThat(reservation.isSeatSelected("A1")).isTrue()
        assertThat(reservation.isSeatSelected("A2")).isFalse()
    }

    @Test
    @DisplayName("선택한 좌석 수만큼 금액을 계산한다")
    fun selectedSeatsAmountWon() {
        val reservation = Reservation.of(testMovie())
            .increaseHeadCount() // 2명
            .selectSeat("A1")

        assertThat(reservation.selectedSeatsAmountWon()).isEqualTo(13_000)
    }
}
