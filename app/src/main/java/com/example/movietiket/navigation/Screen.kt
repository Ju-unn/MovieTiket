package com.example.movietiket.navigation

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.model.Theater

/**
 * 앱의 화면 상태를 표현한다
 * sealed로 선언해 when 분기에서 else 없이 모든 화면을 처리한다
 */
sealed interface Screen {

    /** 하단 네비게이션이 함께 보이는 화면들 */
    sealed interface Tab : Screen {

        /** 예매 내역 탭 */
        data object History : Tab

        /** 홈(영화 목록) 탭 */
        data object Home : Tab

        /** 설정 탭 */
        data object Settings : Tab
    }

    /** 영화 예매 화면 (극장은 영화 목록의 극장 선택 바텀시트에서 미리 고른다) */
    data class MovieReservation(val movie: Movie, val theater: Theater) : Screen

    /** 좌석 선택 화면 */
    data class SeatSelection(val reservation: Reservation) : Screen

    /** 영화 예매 완료 화면 */
    data class ReservationComplete(val reservation: Reservation) : Screen

    /** 예매 내역에서 항목을 눌러 들어온 예매 정보 화면 (완료 화면과 같은 내용을 보여준다) */
    data class ReservationDetail(val reservation: Reservation) : Screen
}
