package com.example.movietiket.seat.presenter

import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SeatSelectionPresenterTest {

    private class FakeView : SeatSelectionContract.View {
        lateinit var shownReservation: Reservation
        var seatLimitExceededCount = 0

        override fun showReservation(reservation: Reservation) {
            shownReservation = reservation
        }

        override fun showSeatLimitExceeded() {
            seatLimitExceededCount++
        }
    }

    @Test
    @DisplayName("생성 시 초기 예매 내용을 View에 통지한다")
    fun initialShowsReservation() {
        val reservation = Reservation.of(testMovie(), testTheater())
        val view = FakeView()

        SeatSelectionPresenter(initialReservation = reservation, view = view, onSelectionConfirmed = {})

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    @Test
    @DisplayName("좌석 선택 시 View에 선택된 좌석을 통지한다")
    fun selectSeat() {
        val view = FakeView()
        val presenter = SeatSelectionPresenter(
            initialReservation = Reservation.of(testMovie(), testTheater()),
            view = view,
            onSelectionConfirmed = {},
        )

        presenter.selectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEqualTo("A1")
    }

    @Test
    @DisplayName("인원수보다 많은 좌석을 선택하면 View에 초과를 통지하고 선택은 반영되지 않는다")
    fun selectSeatBeyondHeadCount() {
        val view = FakeView()
        val presenter = SeatSelectionPresenter(
            initialReservation = Reservation.of(testMovie(), testTheater()), // 1명
            view = view,
            onSelectionConfirmed = {},
        )

        presenter.selectSeat("A1")
        presenter.selectSeat("A2")

        assertThat(view.shownReservation.displaySelectedSeats()).isEqualTo("A1")
        assertThat(view.seatLimitExceededCount).isEqualTo(1)
    }

    @Test
    @DisplayName("좌석 선택을 취소하면 View에 반영된다")
    fun deselectSeat() {
        val view = FakeView()
        val presenter = SeatSelectionPresenter(
            initialReservation = Reservation.of(testMovie(), testTheater()),
            view = view,
            onSelectionConfirmed = {},
        )

        presenter.selectSeat("A1")
        presenter.deselectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    @Test
    @DisplayName("좌석 선택 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmSelection() {
        var confirmed: Reservation? = null
        val presenter = SeatSelectionPresenter(
            initialReservation = Reservation.of(testMovie(), testTheater()),
            view = FakeView(),
            onSelectionConfirmed = { confirmed = it },
        )

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(confirmed?.displaySelectedSeats()).isEqualTo("A1")
    }
}
