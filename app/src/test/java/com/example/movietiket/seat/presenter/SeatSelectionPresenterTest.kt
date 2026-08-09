package com.example.movietiket.seat.presenter

import com.example.movietiket.common.fixture.FakeReservationHistoryRepository
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.Reservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val repository = FakeReservationHistoryRepository()

    // 확정 시 실행되는 코루틴을 테스트에서 즉시 처리한다
    private val scope = CoroutineScope(UnconfinedTestDispatcher())

    private fun presenter(
        view: SeatSelectionContract.View = FakeView(),
        reservation: Reservation = Reservation.of(testMovie(), testTheater()),
        onSelectionConfirmed: (Reservation) -> Unit = {},
    ) = SeatSelectionPresenter(
        initialReservation = reservation,
        view = view,
        reservationHistoryRepository = repository,
        coroutineScope = scope,
        onSelectionConfirmed = onSelectionConfirmed,
    )

    @Test
    @DisplayName("생성 시 초기 예매 내용을 View에 통지한다")
    fun initialShowsReservation() {
        val view = FakeView()

        presenter(view = view)

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    @Test
    @DisplayName("좌석 선택 시 View에 선택된 좌석을 통지한다")
    fun selectSeat() {
        val view = FakeView()
        val presenter = presenter(view = view)

        presenter.selectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEqualTo("A1")
    }

    @Test
    @DisplayName("인원수보다 많은 좌석을 선택하면 View에 초과를 통지하고 선택은 반영되지 않는다")
    fun selectSeatBeyondHeadCount() {
        val view = FakeView()
        val presenter = presenter(view = view) // 1명

        presenter.selectSeat("A1")
        presenter.selectSeat("A2")

        assertThat(view.shownReservation.displaySelectedSeats()).isEqualTo("A1")
        assertThat(view.seatLimitExceededCount).isEqualTo(1)
    }

    @Test
    @DisplayName("좌석 선택을 취소하면 View에 반영된다")
    fun deselectSeat() {
        val view = FakeView()
        val presenter = presenter(view = view)

        presenter.selectSeat("A1")
        presenter.deselectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    @Test
    @DisplayName("좌석 선택 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmSelection() {
        var confirmed: Reservation? = null
        val presenter = presenter(onSelectionConfirmed = { confirmed = it })

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(confirmed?.displaySelectedSeats()).isEqualTo("A1")
    }

    @Test
    @DisplayName("좌석 선택 확정 시 예매 내역을 로컬 DB에 저장한다")
    fun confirmSelectionSavesHistory() {
        val presenter = presenter()

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(repository.saved).hasSize(1)
        assertThat(repository.saved.first().displaySelectedSeats()).isEqualTo("A1")
    }

    @Test
    @DisplayName("저장이 끝난 뒤에 완료 화면으로 넘긴다")
    fun savesBeforeNavigating() {
        var savedCountWhenConfirmed = -1
        val presenter = presenter(onSelectionConfirmed = { savedCountWhenConfirmed = repository.saved.size })

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(savedCountWhenConfirmed).isEqualTo(1)
    }
}
