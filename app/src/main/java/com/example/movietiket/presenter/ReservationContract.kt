package com.example.movietiket.presenter

import com.example.movietiket.model.Reservation

/**
 * 영화 예매 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface ReservationContract {

    interface View {
        fun showReservation(reservation: Reservation)
    }

    interface Presenter {
        fun increaseHeadCount()
        fun decreaseHeadCount()
        fun selectTime(time: String)
        fun confirmReservation()
    }
}
