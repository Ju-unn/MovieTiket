package com.example.movietiket.seat.presenter

import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.repository.ReservationHistoryRepository
import com.example.movietiket.notification.ScreeningAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 좌석 선택 화면의 흐름을 제어하는 Presenter
 * 좌석 선택/해제 요청을 도메인(Reservation)에 위임하고, 인원수 초과 시 View에 통지한다
 * 예매를 확정하면 로컬 DB에 내역을 남긴 뒤 완료 화면으로 넘긴다
 */
class SeatSelectionPresenter(
    initialReservation: Reservation,
    private val view: SeatSelectionContract.View,
    private val reservationHistoryRepository: ReservationHistoryRepository,
    private val screeningAlarmScheduler: ScreeningAlarmScheduler,
    private val coroutineScope: CoroutineScope,
    private val onSelectionConfirmed: (Reservation) -> Unit,
) : SeatSelectionContract.Presenter {

    private var reservation: Reservation = initialReservation
        set(value) {
            field = value
            view.showReservation(value)
        }

    init {
        view.showReservation(reservation)
    }

    override fun selectSeat(seat: String) {
        reservation = try {
            reservation.selectSeat(seat)
        } catch (e: IllegalArgumentException) {
            view.showSeatLimitExceeded()
            return
        }
    }

    override fun deselectSeat(seat: String) {
        reservation = reservation.deselectSeat(seat)
    }

    override fun confirmSelection() {
        // 저장이 끝난 뒤 화면을 넘겨야 완료 화면에서 되돌아왔을 때 내역이 이미 보인다
        coroutineScope.launch {
            val reservationId = reservationHistoryRepository.save(reservation)
            screeningAlarmScheduler.schedule(reservationId, reservation)
            onSelectionConfirmed(reservation)
        }
    }
}
