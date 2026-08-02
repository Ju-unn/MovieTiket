package com.example.movietiket.navigation

import androidx.compose.runtime.MutableState
import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation

/**
 * 화면 전환을 담당하는 Router
 * 화면 상태는 외부(rememberSaveable)에서 주입받아 회전 등 구성 변경에도 유지되도록 한다
 */
class MovieNavigationController(private val screenState: MutableState<Screen>) {

    fun screen(): Screen = screenState.value

    fun moveToMovieList() {
        screenState.value = Screen.MovieList
    }

    fun moveToReservation(movie: Movie) {
        screenState.value = Screen.MovieReservation(movie)
    }

    fun moveToSeatSelection(reservation: Reservation) {
        screenState.value = Screen.SeatSelection(reservation)
    }

    fun moveToReservationComplete(reservation: Reservation) {
        screenState.value = Screen.ReservationComplete(reservation)
    }
}
