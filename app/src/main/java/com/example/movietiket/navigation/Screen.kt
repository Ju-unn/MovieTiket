package com.example.movietiket.navigation

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Reservation

/**
 * 앱의 화면 상태를 표현한다
 * sealed로 선언해 when 분기에서 else 없이 모든 화면을 처리한다
 */
sealed interface Screen {

    /** 영화 목록 화면 */
    data object MovieList : Screen

    /** 영화 예매 화면 */
    data class MovieReservation(val movie: Movie) : Screen

    /** 좌석 선택 화면 */
    data class SeatSelection(val reservation: Reservation) : Screen

    /** 영화 예매 완료 화면 */
    data class ReservationComplete(val reservation: Reservation) : Screen
}
