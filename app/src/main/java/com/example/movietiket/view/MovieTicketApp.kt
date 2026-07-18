package com.example.movietiket.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.movietiket.controller.MovieListController
import com.example.movietiket.controller.MovieNavigationController
import com.example.movietiket.controller.ReservationController
import com.example.movietiket.controller.Screen
import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation
import com.example.movietiket.view.complete.ReservationCompleteScreen
import com.example.movietiket.view.movielist.MovieListScreen
import com.example.movietiket.view.reservation.MovieReservationScreen

/**
 * 앱의 루트 Composable
 * 현재 화면 상태(Screen)에 따라 각 화면(View)과 컨트롤러를 연결한다
 */
@Composable
fun MovieTicketApp() {
    val navigationController = remember { MovieNavigationController() }

    when (val screen = navigationController.screen()) {
        is Screen.MovieList -> MovieListRoute(navigationController)
        is Screen.MovieReservation -> MovieReservationRoute(screen.movie, navigationController)
        is Screen.ReservationComplete -> ReservationCompleteRoute(screen.reservation, navigationController)
    }
}

@Composable
private fun MovieListRoute(navigationController: MovieNavigationController) {
    val movieListController = remember { MovieListController() }

    MovieListScreen(
        movies = movieListController.movies(),
        onReserveClick = { movie -> navigationController.moveToReservation(movie) },
    )
}

@Composable
private fun MovieReservationRoute(
    movie: Movie,
    navigationController: MovieNavigationController,
) {
    val reservationController = remember { ReservationController(movie) }

    // 시스템 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    MovieReservationScreen(
        reservation = reservationController.reservation(),
        onIncreaseHeadCount = reservationController::increaseHeadCount,
        onDecreaseHeadCount = reservationController::decreaseHeadCount,
        onConfirmClick = {
            navigationController.moveToReservationComplete(reservationController.reservation())
        },
        onBackClick = navigationController::moveToMovieList,
    )
}

@Composable
private fun ReservationCompleteRoute(
    reservation: Reservation,
    navigationController: MovieNavigationController,
) {
    // 완료 화면에서 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    ReservationCompleteScreen(
        reservation = reservation,
        onBackClick = navigationController::moveToMovieList,
    )
}
