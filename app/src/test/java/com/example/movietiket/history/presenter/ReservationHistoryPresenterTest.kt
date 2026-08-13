package com.example.movietiket.history.presenter

import com.example.movietiket.common.fixture.FakeReservationHistoryRepository
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.model.ReservationHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReservationHistoryPresenterTest {

    private class FakeView : ReservationHistoryContract.View {
        var shownHistories: List<ReservationHistory> = emptyList()

        override fun showHistories(histories: List<ReservationHistory>) {
            shownHistories = histories
        }
    }

    private val repository = FakeReservationHistoryRepository()
    private val scope = CoroutineScope(UnconfinedTestDispatcher())

    private fun reservation(seat: String): Reservation =
        Reservation.of(testMovie(), testTheater()).selectSeat(seat)

    @Test
    @DisplayName("생성 시 저장된 예매 내역을 View에 통지한다")
    fun loadsSavedHistories() = runTest {
        repository.save(reservation("A1"))
        val view = FakeView()

        ReservationHistoryPresenter(view, repository, scope, onHistorySelected = {})

        assertThat(view.shownHistories).hasSize(1)
        assertThat(view.shownHistories.first().displaySelectedSeatsForTest()).isEqualTo("A1")
    }

    @Test
    @DisplayName("예매가 새로 저장되면 목록이 자동으로 갱신된다")
    fun updatesWhenNewReservationSaved() = runTest {
        val view = FakeView()
        ReservationHistoryPresenter(view, repository, scope, onHistorySelected = {})

        repository.save(reservation("B3"))

        assertThat(view.shownHistories).hasSize(1)
    }

    @Test
    @DisplayName("내역을 클릭하면 해당 예매로 콜백을 호출한다")
    fun onHistoryClick() = runTest {
        repository.save(reservation("A1"))
        var selected: ReservationHistory? = null
        val view = FakeView()
        val presenter = ReservationHistoryPresenter(
            view, repository, scope, onHistorySelected = { selected = it },
        )

        presenter.onHistoryClick(view.shownHistories.first())

        assertThat(selected?.id()).isEqualTo(view.shownHistories.first().id())
    }

    private fun ReservationHistory.displaySelectedSeatsForTest(): String =
        reservation().displaySelectedSeats()
}
