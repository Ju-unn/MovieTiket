package com.example.movietiket.presenter

import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation

/**
 * 영화 예매 화면의 흐름을 제어하는 Presenter
 * 인원 증감 요청을 받아 도메인(Reservation)에 위임하고, 결과를 View에 통지한다
 */
class ReservationPresenter(
    movie: Movie,
    private val view: ReservationContract.View,
    private val onReservationConfirmed: (Reservation) -> Unit,
) : ReservationContract.Presenter {

    private var reservation: Reservation = Reservation.of(movie)
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

    override fun selectTime(time: String) {
        reservation = reservation.selectTime(time)
    }

    override fun confirmReservation() {
        onReservationConfirmed(reservation)
    }
}
