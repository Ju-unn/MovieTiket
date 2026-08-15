package com.example.movietiket.seat.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.repository.ReservationHistoryRepository
import com.example.movietiket.common.state.ReservationSaver
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.notification.ScreeningAlarmScheduler
import com.example.movietiket.seat.presenter.SeatSelectionContract
import com.example.movietiket.seat.presenter.SeatSelectionPresenter

// 좌석 선택 화면을 보여준다
@Composable
internal fun SeatSelectionRoute(
    initialReservation: Reservation,
    reservationHistoryRepository: ReservationHistoryRepository,
    screeningAlarmScheduler: ScreeningAlarmScheduler,
    navigationController: MovieNavigationController,
) {
    val coroutineScope = rememberCoroutineScope()
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
            reservationHistoryRepository = reservationHistoryRepository,
            screeningAlarmScheduler = screeningAlarmScheduler,
            coroutineScope = coroutineScope,
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
