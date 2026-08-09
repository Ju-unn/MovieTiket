package com.example.movietiket.reservation.presenter

import com.example.movietiket.common.model.Reservation
import java.time.LocalDate
import java.time.LocalTime

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
        fun selectDate(date: LocalDate)
        fun selectTime(time: LocalTime)
        fun confirmReservation()
    }
}
