package com.example.movietiket.presenter
import com.example.movietiket.fixture.testMovie
import com.example.movietiket.model.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ReservationPresenterTest {

    private class FakeView : ReservationContract.View {
        lateinit var shownReservation: Reservation
        override fun showReservation(reservation: Reservation) {
            shownReservation = reservation
        }
    }

    @Test
    @DisplayName("생성 시 최소 인원 1명인 예매를 View에 통지한다")
    fun initialShowsMinimumHeadCount() {
        val view = FakeView()

        ReservationPresenter(movie = testMovie(), view = view, onReservationConfirmed = {})

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("인원 증가 시 View에 증가한 인원을 통지한다")
    fun increaseHeadCount() {
        val view = FakeView()
        val presenter = ReservationPresenter(movie = testMovie(), view = view, onReservationConfirmed = {})

        presenter.increaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 감소 시 View에 감소한 인원을 통지한다")
    fun decreaseHeadCount() {
        val view = FakeView()
        val presenter = ReservationPresenter(movie = testMovie(), view = view, onReservationConfirmed = {})

        presenter.increaseHeadCount()
        presenter.increaseHeadCount()
        presenter.decreaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("시간 선택 시 View에 선택한 시간을 통지한다")
    fun selectTime() {
        val view = FakeView()
        val presenter = ReservationPresenter(movie = testMovie(), view = view, onReservationConfirmed = {})

        presenter.selectTime("13:00")

        assertThat(view.shownReservation.displaySelectedTime()).isEqualTo("13:00")
    }

    @Test
    @DisplayName("예매 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmReservation() {
        var confirmed: Reservation? = null
        val presenter = ReservationPresenter(
            movie = testMovie(),
            view = FakeView(),
            onReservationConfirmed = { confirmed = it },
        )

        presenter.increaseHeadCount()
        presenter.confirmReservation()

        assertThat(confirmed?.displayHeadCount()).isEqualTo("2")
    }
}
