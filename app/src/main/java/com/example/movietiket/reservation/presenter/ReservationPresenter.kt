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

    // 인원 수를 늘리고 변경된 예매 정보를 반영한다
    override fun increaseHeadCount() {
        reservation = reservation.increaseHeadCount()
    }

    // 인원 수를 줄이고 변경된 예매 정보를 반영한다
    override fun decreaseHeadCount() {
        reservation = reservation.decreaseHeadCount()
    }

    // 상영 날짜를 선택하고 변경된 예매 정보를 반영한다
    override fun selectDate(date: LocalDate) {
        reservation = reservation.selectDate(date)
    }

    // 상영 시간을 선택하고 변경된 예매 정보를 반영한다
    override fun selectTime(time: LocalTime) {
        reservation = reservation.selectTime(time)
    }

    // 예매를 확정하고 결과를 콜백으로 전달한다
    override fun confirmReservation() {
        onReservationConfirmed(reservation)
    }
}
