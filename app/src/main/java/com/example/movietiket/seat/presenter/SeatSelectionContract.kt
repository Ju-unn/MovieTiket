package com.example.movietiket.seat.presenter

import com.example.movietiket.common.model.reservation.Reservation

/**
 * 좌석 선택 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface SeatSelectionContract {

    /** 좌석 선택 화면에 상태를 표시하는 View */
    interface View {
        // 예매 정보를 화면에 표시한다
        fun showReservation(reservation: Reservation)
        // 선택 가능 인원 초과를 알린다
        fun showSeatLimitExceeded()
    }

    /** 좌석 선택 화면의 사용자 입력을 처리하는 Presenter */
    interface Presenter {
        // 좌석을 선택한다
        fun selectSeat(seat: String)
        // 좌석 선택을 해제한다
        fun deselectSeat(seat: String)
        // 좌석 선택을 확정한다
        fun confirmSelection()
    }
}
