package com.example.movietiket.seat.presenter

import com.example.movietiket.common.fixture.FakeReservationHistoryRepository
import com.example.movietiket.common.fixture.FakeScreeningAlarmScheduler
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 좌석 선택 프레젠터의 좌석 선택/취소, 인원 초과 처리, 확정 시 저장·알림 예약 흐름을 검증한다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SeatSelectionPresenterTest {

    // 테스트용 View 구현체: 예매 내용 및 좌석 초과 통지 횟수를 저장해 검증에 사용
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
    private val alarmScheduler = FakeScreeningAlarmScheduler()

    // 확정 시 실행되는 코루틴을 테스트에서 즉시 처리한다
    private val scope = CoroutineScope(UnconfinedTestDispatcher())

    // 테스트용 좌석 선택 프레젠터 생성
    private fun presenter(
        view: SeatSelectionContract.View = FakeView(),
        reservation: Reservation = Reservation.of(testMovie(), testTheater()),
        onSelectionConfirmed: (Reservation) -> Unit = {},
    ) = SeatSelectionPresenter(
        initialReservation = reservation,
        view = view,
        reservationHistoryRepository = repository,
        screeningAlarmScheduler = alarmScheduler,
        coroutineScope = scope,
        onSelectionConfirmed = onSelectionConfirmed,
    )

    // 생성 시 초기 예매 내용(선택된 좌석 없음)이 View에 통지되는지 검증
    @Test
    @DisplayName("생성 시 초기 예매 내용을 View에 통지한다")
    fun initialShowsReservation() {
        val view = FakeView()

        presenter(view = view)

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    // 좌석 선택 시 선택된 좌석이 View에 통지되는지 검증
    @Test
    @DisplayName("좌석 선택 시 View에 선택된 좌석을 통지한다")
    fun selectSeat() {
        val view = FakeView()
        val presenter = presenter(view = view)

        presenter.selectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEqualTo("A1")
    }

    // 인원수보다 많은 좌석 선택 시 초과 통지되고 선택이 반영되지 않는지 검증
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

    // 좌석 선택 취소가 View에 반영되는지 검증
    @Test
    @DisplayName("좌석 선택을 취소하면 View에 반영된다")
    fun deselectSeat() {
        val view = FakeView()
        val presenter = presenter(view = view)

        presenter.selectSeat("A1")
        presenter.deselectSeat("A1")

        assertThat(view.shownReservation.displaySelectedSeats()).isEmpty()
    }

    // 좌석 선택 확정 시 현재 예매 내용으로 콜백이 호출되는지 검증
    @Test
    @DisplayName("좌석 선택 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmSelection() {
        var confirmed: Reservation? = null
        val presenter = presenter(onSelectionConfirmed = { confirmed = it })

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(confirmed?.displaySelectedSeats()).isEqualTo("A1")
    }

    // 좌석 선택 확정 시 예매 내역이 로컬 DB에 저장되는지 검증
    @Test
    @DisplayName("좌석 선택 확정 시 예매 내역을 로컬 DB에 저장한다")
    fun confirmSelectionSavesHistory() {
        val presenter = presenter()

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(repository.saved).hasSize(1)
        assertThat(repository.saved.first().displaySelectedSeats()).isEqualTo("A1")
    }

    // 좌석 선택 확정 시 저장된 예매 id로 상영 알림이 예약되는지 검증
    @Test
    @DisplayName("좌석 선택 확정 시 저장된 예매 id로 상영 알림을 예약한다")
    fun confirmSelectionSchedulesAlarm() {
        val presenter = presenter()

        presenter.selectSeat("A1")
        presenter.confirmSelection()

        assertThat(alarmScheduler.scheduled).hasSize(1)
        val (reservationId, reserved) = alarmScheduler.scheduled.first()
        assertThat(reservationId).isEqualTo(1L)
        assertThat(reserved.displaySelectedSeats()).isEqualTo("A1")
    }

    // 저장이 끝난 뒤에야 완료 화면으로 넘어가는지(저장-후-콜백 순서) 검증
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
