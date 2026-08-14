package com.example.movietiket.reservation.presenter

import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationPresenterTest {

    private class FakeView : ReservationContract.View {
        lateinit var shownReservation: Reservation
        override fun showReservation(reservation: Reservation) {
            shownReservation = reservation
        }
    }

    private fun presenter(view: ReservationContract.View, onReservationConfirmed: (Reservation) -> Unit = {}) =
        ReservationPresenter(
            initialReservation = Reservation.of(testMovie(), testTheater()),
            view = view,
            onReservationConfirmed = onReservationConfirmed,
        )

    @Test
    @DisplayName("생성 시 초기 예매 내용(최소 인원 1명)을 View에 통지한다")
    fun initialShowsReservation() {
        val view = FakeView()

        presenter(view)

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("인원 증가 시 View에 증가한 인원을 통지한다")
    fun increaseHeadCount() {
        val view = FakeView()
        val presenter = presenter(view)

        presenter.increaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 감소 시 View에 감소한 인원을 통지한다")
    fun decreaseHeadCount() {
        val view = FakeView()
        val presenter = presenter(view)

        presenter.increaseHeadCount()
        presenter.increaseHeadCount()
        presenter.decreaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("날짜 선택 시 View에 선택한 날짜를 통지한다")
    fun selectDate() {
        val view = FakeView()
        val presenter = presenter(view)

        presenter.selectDate(LocalDate.of(2024, 3, 2))

        assertThat(view.shownReservation.displaySelectedDate()).isEqualTo("2024.3.2")
    }

    @Test
    @DisplayName("시간 선택 시 View에 선택한 시간을 통지한다")
    fun selectTime() {
        val view = FakeView()
        val presenter = presenter(view)

        presenter.selectTime(LocalTime.of(20, 0))

        assertThat(view.shownReservation.displaySelectedTime()).isEqualTo("20:00")
    }

    @Test
    @DisplayName("예매 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmReservation() {
        var confirmed: Reservation? = null
        val presenter = presenter(FakeView()) { confirmed = it }

        presenter.increaseHeadCount()
        presenter.confirmReservation()

        assertThat(confirmed?.displayHeadCount()).isEqualTo("2")
    }
}
