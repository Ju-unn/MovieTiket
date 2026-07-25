package com.example.movietiket.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation

/**
 * 영화 예매 화면의 흐름을 제어하는 컨트롤러
 * 인원 증감 요청을 받아 도메인(Reservation)에 위임한다
 */

class ReservationController(movie: Movie) {

    private var reservation: Reservation by mutableStateOf(Reservation.of(movie))

    fun reservation(): Reservation = reservation

    fun increaseHeadCount() {
        reservation = reservation.increaseHeadCount()
    }

    fun decreaseHeadCount() {
        reservation = reservation.decreaseHeadCount()
    }
}
