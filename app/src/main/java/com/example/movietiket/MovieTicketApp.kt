package com.example.movietiket

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.navigation.Screen
import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.movielist.presenter.MovieListContract
import com.example.movietiket.movielist.presenter.MovieListPresenter
import com.example.movietiket.reservation.presenter.ReservationContract
import com.example.movietiket.reservation.presenter.ReservationPresenter
import com.example.movietiket.seat.presenter.SeatSelectionContract
import com.example.movietiket.seat.presenter.SeatSelectionPresenter
import com.example.movietiket.common.state.ReservationSaver
import com.example.movietiket.common.state.ScreenSaver
import com.example.movietiket.complete.view.ReservationCompleteScreen
import com.example.movietiket.movielist.view.MovieListScreen
import com.example.movietiket.reservation.view.MovieReservationScreen
import com.example.movietiket.seat.view.SeatSelectionScreen

/**
 * 앱의 루트 Composable
 * 현재 화면 상태(Screen)에 따라 각 화면(View)과 Presenter를 연결한다
 * 화면 상태는 rememberSaveable로 저장해 회전 등 구성 변경에도 유지한다
 */
@Composable
fun MovieTicketApp() {
    val screenState = rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.MovieList) }
    val navigationController = remember { MovieNavigationController(screenState) }

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
            onMovieReserveRequested = navigationController::moveToReservation,
        )
    }

    MovieListScreen(
        movies = view.movies,
        onReserveClick = presenter::onReserveClick,
    )
}

@Composable
private fun MovieReservationRoute(
    movie: Movie,
    navigationController: MovieNavigationController,
) {
    var reservation by rememberSaveable(stateSaver = ReservationSaver) { mutableStateOf(Reservation.of(movie)) }
    val view = remember {
        object : ReservationContract.View {
            override fun showReservation(newReservation: Reservation) {
                reservation = newReservation
            }
        }
    }
    // 시스템 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    val presenter = remember(view) {
        ReservationPresenter(
            initialReservation = reservation,
            view = view,
            onReservationConfirmed = navigationController::moveToSeatSelection,
        )
    }

    MovieReservationScreen(
        reservation = reservation,
        onIncreaseHeadCount = presenter::increaseHeadCount,
        onDecreaseHeadCount = presenter::decreaseHeadCount,
        onSelectDate = presenter::selectDate,
        onSelectTime = presenter::selectTime,
        onConfirmClick = presenter::confirmReservation,
        onBackClick = navigationController::moveToMovieList,
    )
}

@Composable
private fun SeatSelectionRoute(
    initialReservation: Reservation,
    navigationController: MovieNavigationController,
) {
    var reservation by rememberSaveable(stateSaver = ReservationSaver) { mutableStateOf(initialReservation) }
    var seatLimitExceededEventCount by remember { mutableStateOf(0) }
    val view = remember {
        object : SeatSelectionContract.View {
            override fun showReservation(newReservation: Reservation) {
                reservation = newReservation
            }

            override fun showSeatLimitExceeded() {
                seatLimitExceededEventCount++
            }
        }
    }

    // 좌석 선택 화면에서 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    val presenter = remember(view) {
        SeatSelectionPresenter(
            initialReservation = reservation,
            view = view,
            onSelectionConfirmed = navigationController::moveToReservationComplete,
        )
    }

    SeatSelectionScreen(
        reservation = reservation,
        seatLimitExceededEvent = seatLimitExceededEventCount,
        onSelectSeat = presenter::selectSeat,
        onDeselectSeat = presenter::deselectSeat,
        onConfirmClick = presenter::confirmSelection,
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
