package com.example.movietiket.presenter

import com.example.movietiket.model.Reservation

/**
 * 좌석 선택 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface SeatSelectionContract {

    interface View {
        fun showReservation(reservation: Reservation)
        fun showSeatLimitExceeded()
    }

    interface Presenter {
        fun selectSeat(seat: String)
        fun deselectSeat(seat: String)
        fun confirmSelection()
    }
}
