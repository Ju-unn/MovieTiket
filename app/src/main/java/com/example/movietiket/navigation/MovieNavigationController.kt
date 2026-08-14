package com.example.movietiket.navigation

import androidx.compose.runtime.MutableState
import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.theater.Theater

/**
 * 화면 전환을 담당하는 Router
 * 화면 상태는 외부(rememberSaveable)에서 주입받아 회전 등 구성 변경에도 유지되도록 한다
 */
class MovieNavigationController(private val screenState: MutableState<Screen>) {

    // 현재 화면 상태를 반환
    fun screen(): Screen = screenState.value

    // 하단 네비게이션 탭으로 이동
    fun moveToTab(tab: Screen.Tab) {
        screenState.value = tab
    }

    // 홈(영화 목록) 탭으로 이동
    fun moveToMovieList() {
        screenState.value = Screen.Tab.Home
    }

    // 영화 예매 화면으로 이동
    fun moveToReservation(movie: Movie, theater: Theater) {
        screenState.value = Screen.MovieReservation(movie, theater)
    }

    // 좌석 선택 화면으로 이동
    fun moveToSeatSelection(reservation: Reservation) {
        screenState.value = Screen.SeatSelection(reservation)
    }

    // 예매 완료 화면으로 이동
    fun moveToReservationComplete(reservation: Reservation) {
        screenState.value = Screen.ReservationComplete(reservation)
    }

    // 예매 상세(내역 클릭) 화면으로 이동
    fun moveToReservationDetail(reservation: Reservation) {
        screenState.value = Screen.ReservationDetail(reservation)
    }
}
