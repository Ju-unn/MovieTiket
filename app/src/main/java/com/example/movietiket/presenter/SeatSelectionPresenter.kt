package com.example.movietiket.presenter

import com.example.movietiket.model.Reservation

/**
 * 좌석 선택 화면의 흐름을 제어하는 Presenter
 * 좌석 선택/해제 요청을 도메인(Reservation)에 위임하고, 인원수 초과 시 View에 통지한다
 */
class SeatSelectionPresenter(
    initialReservation: Reservation,
    private val view: SeatSelectionContract.View,
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
        onSelectionConfirmed(reservation)
    }
}
