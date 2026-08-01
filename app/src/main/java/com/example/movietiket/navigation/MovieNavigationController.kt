package com.example.movietiket.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation

/**
 * 화면 전환을 담당하는 Router
 * View는 이 Router에 전환을 요청하고, 현재 화면은 screen()으로만 조회한다
 */
class MovieNavigationController {

    private var currentScreen: Screen by mutableStateOf(Screen.MovieList)

    fun screen(): Screen = currentScreen

    fun moveToMovieList() {
        currentScreen = Screen.MovieList
    }

    fun moveToReservation(movie: Movie) {
        currentScreen = Screen.MovieReservation(movie)
    }

    fun moveToSeatSelection(reservation: Reservation) {
        currentScreen = Screen.SeatSelection(reservation)
    }

    fun moveToReservationComplete(reservation: Reservation) {
        currentScreen = Screen.ReservationComplete(reservation)
    }
}
