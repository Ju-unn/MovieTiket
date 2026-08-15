package com.example.movietiket.complete.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.navigation.Screen

// 예매 완료 화면을 보여준다
@Composable
internal fun ReservationCompleteRoute(
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

// 예매 상세 정보 화면을 보여준다 (완료 화면과 같은 내용을 예매 내역에서 다시 본다)
@Composable
internal fun ReservationDetailRoute(
    reservation: Reservation,
    navigationController: MovieNavigationController,
) {
    // 예매 내역에서 들어왔으므로 뒤로 가면 예매 내역 탭으로 돌아간다
    val backToHistory = { navigationController.moveToTab(Screen.Tab.History) }
    BackHandler { backToHistory() }

    ReservationCompleteScreen(reservation = reservation, onBackClick = backToHistory)
}
