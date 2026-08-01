package com.example.movietiket.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.navigation.Screen
import com.example.movietiket.model.Movie
import com.example.movietiket.model.Reservation
import com.example.movietiket.presenter.MovieListContract
import com.example.movietiket.presenter.MovieListPresenter
import com.example.movietiket.presenter.ReservationContract
import com.example.movietiket.presenter.ReservationPresenter
import com.example.movietiket.repository.MovieRepository
import com.example.movietiket.view.complete.ReservationCompleteScreen
import com.example.movietiket.view.movielist.MovieListScreen
import com.example.movietiket.view.reservation.MovieReservationScreen
import com.example.movietiket.view.seat.SeatSelectionScreen

/**
 * 앱의 루트 Composable
 * 현재 화면 상태(Screen)에 따라 각 화면(View)과 Presenter를 연결한다
 */
@Composable
fun MovieTicketApp() {
    val navigationController = remember { MovieNavigationController() }

    when (val screen = navigationController.screen()) {
        is Screen.MovieList -> MovieListRoute(navigationController)
        is Screen.MovieReservation -> MovieReservationRoute(screen.movie, navigationController)
        is Screen.SeatSelection -> SeatSelectionRoute(screen.reservation, navigationController)
        is Screen.ReservationComplete -> ReservationCompleteRoute(screen.reservation, navigationController)
    }
}

private class MovieListViewState : MovieListContract.View {
    var movies by mutableStateOf(emptyList<Movie>())
        private set

    override fun showMovies(movies: List<Movie>) {
        this.movies = movies
    }
}

@Composable
private fun MovieListRoute(navigationController: MovieNavigationController) {
    val view = remember { MovieListViewState() }
    val presenter = remember {
        MovieListPresenter(
            view = view,
            movies = MovieRepository.findAll(),
            onMovieReserveRequested = navigationController::moveToReservation,
        )
    }

    MovieListScreen(
        movies = view.movies,
        onReserveClick = presenter::onReserveClick,
    )
}

private class ReservationViewState : ReservationContract.View {
    var reservation: Reservation? by mutableStateOf(null)
        private set

    override fun showReservation(reservation: Reservation) {
        this.reservation = reservation
    }
}

@Composable
private fun MovieReservationRoute(
    movie: Movie,
    navigationController: MovieNavigationController,
) {
    val view = remember { ReservationViewState() }
    val presenter = remember {
        ReservationPresenter(
            movie = movie,
            view = view,
            onReservationConfirmed = navigationController::moveToSeatSelection,
        )
    }
    val reservation = view.reservation ?: return

    // 시스템 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    MovieReservationScreen(
        reservation = reservation,
        onIncreaseHeadCount = presenter::increaseHeadCount,
        onDecreaseHeadCount = presenter::decreaseHeadCount,
        onConfirmClick = presenter::confirmReservation,
        onBackClick = navigationController::moveToMovieList,
    )
}

@Composable
private fun SeatSelectionRoute(
    reservation: Reservation,
    navigationController: MovieNavigationController,
) {
    // 좌석 선택 화면에서 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    SeatSelectionScreen(
        movieTitle = reservation.displayMovieTitle(),
        pricePerSeat = reservation.ticketPriceWon(),
        onBackClick = navigationController::moveToMovieList,
        onConfirmClick = { navigationController.moveToReservationComplete(reservation) },
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
