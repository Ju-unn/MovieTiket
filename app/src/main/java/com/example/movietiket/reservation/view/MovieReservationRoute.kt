package com.example.movietiket.reservation.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.state.ReservationSaver
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.reservation.presenter.ReservationContract
import com.example.movietiket.reservation.presenter.ReservationPresenter

// 영화 예매(인원/날짜/시간 선택) 화면을 보여준다
@Composable
internal fun MovieReservationRoute(
    movie: Movie,
    theater: Theater,
    navigationController: MovieNavigationController,
) {
    var reservation by rememberSaveable(stateSaver = ReservationSaver) { mutableStateOf(Reservation.of(movie, theater)) }
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
