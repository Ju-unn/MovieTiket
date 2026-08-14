package com.example.movietiket.history.presenter

import com.example.movietiket.common.fixture.FakeReservationHistoryRepository
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 예매 내역 프레젠터가 저장소 변화를 View에 반영하고 클릭 콜백을 처리하는지 검증한다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReservationHistoryPresenterTest {

    // 테스트용 View 구현체: 전달받은 예매 내역을 저장해 검증에 사용
    private class FakeView : ReservationHistoryContract.View {
        var shownHistories: List<ReservationHistory> = emptyList()

        override fun showHistories(histories: List<ReservationHistory>) {
            shownHistories = histories
        }
    }

    private val repository = FakeReservationHistoryRepository()
    private val scope = CoroutineScope(UnconfinedTestDispatcher())

    // 좌석을 선택한 테스트용 예매를 생성
    private fun reservation(seat: String): Reservation =
        Reservation.of(testMovie(), testTheater()).selectSeat(seat)

    // 프레젠터 생성 시 저장된 예매 내역이 View에 통지되는지 검증
    @Test
    @DisplayName("생성 시 저장된 예매 내역을 View에 통지한다")
    fun loadsSavedHistories() = runTest {
        repository.save(reservation("A1"))
        val view = FakeView()

        ReservationHistoryPresenter(view, repository, scope, onHistorySelected = {})

        assertThat(view.shownHistories).hasSize(1)
        assertThat(view.shownHistories.first().displaySelectedSeatsForTest()).isEqualTo("A1")
    }

    // 새 예매가 저장되면 목록이 자동으로 갱신되는지 검증
    @Test
    @DisplayName("예매가 새로 저장되면 목록이 자동으로 갱신된다")
    fun updatesWhenNewReservationSaved() = runTest {
        val view = FakeView()
        ReservationHistoryPresenter(view, repository, scope, onHistorySelected = {})

        repository.save(reservation("B3"))

        assertThat(view.shownHistories).hasSize(1)
    }

    // 내역 클릭 시 해당 예매로 콜백이 호출되는지 검증
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

    // 테스트 검증용으로 선택된 좌석 표시값을 조회
    private fun ReservationHistory.displaySelectedSeatsForTest(): String =
        reservation().displaySelectedSeats()
}
