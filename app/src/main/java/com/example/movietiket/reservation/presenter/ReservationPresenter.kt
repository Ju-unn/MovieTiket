package com.example.movietiket.reservation.presenter

import com.example.movietiket.common.model.reservation.Reservation
import java.time.LocalDate
import java.time.LocalTime

/**
 * 영화 예매 화면의 흐름을 제어하는 Presenter
 * 인원/날짜/시간 변경 요청을 받아 도메인(Reservation)에 위임하고, 결과를 View에 통지한다
 */
class ReservationPresenter(
    initialReservation: Reservation,
    private val view: ReservationContract.View,
    private val onReservationConfirmed: (Reservation) -> Unit,
) : ReservationContract.Presenter {

    private var reservation: Reservation = initialReservation
        set(value) {
            field = value
            view.showReservation(value)
        }

    init {
        view.showReservation(reservation)
    }

    override fun increaseHeadCount() {
        reservation = reservation.increaseHeadCount()
    }

    override fun decreaseHeadCount() {
        reservation = reservation.decreaseHeadCount()
    }

    override fun selectDate(date: LocalDate) {
        reservation = reservation.selectDate(date)
    }

    override fun selectTime(time: LocalTime) {
        reservation = reservation.selectTime(time)
    }

    override fun confirmReservation() {
        onReservationConfirmed(reservation)
    }
}
