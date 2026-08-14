package com.example.movietiket.navigation

import androidx.compose.runtime.mutableStateOf
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieNavigationControllerTest {

    @Test
    @DisplayName("현재 화면 상태를 그대로 조회할 수 있다")
    fun screen() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))

        assertThat(controller.screen()).isEqualTo(Screen.Tab.Home)
    }

    @Test
    @DisplayName("탭으로 이동하면 화면 상태가 그 탭으로 바뀐다")
    fun moveToTab() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))

        controller.moveToTab(Screen.Tab.Settings)

        assertThat(controller.screen()).isEqualTo(Screen.Tab.Settings)
    }

    @Test
    @DisplayName("영화 목록으로 이동하면 홈 탭으로 바뀐다")
    fun moveToMovieList() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Settings))

        controller.moveToMovieList()

        assertThat(controller.screen()).isEqualTo(Screen.Tab.Home)
    }

    @Test
    @DisplayName("예매 화면으로 이동하면 선택한 영화/극장이 담긴 화면으로 바뀐다")
    fun moveToReservation() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))
        val movie = testMovie()
        val theater = testTheater()

        controller.moveToReservation(movie, theater)

        assertThat(controller.screen()).isEqualTo(Screen.MovieReservation(movie, theater))
    }

    @Test
    @DisplayName("좌석 선택 화면으로 이동하면 예매 정보가 담긴 화면으로 바뀐다")
    fun moveToSeatSelection() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))
        val reservation = Reservation.of(testMovie(), testTheater())

        controller.moveToSeatSelection(reservation)

        assertThat(controller.screen()).isEqualTo(Screen.SeatSelection(reservation))
    }

    @Test
    @DisplayName("예매 완료 화면으로 이동하면 예매 정보가 담긴 화면으로 바뀐다")
    fun moveToReservationComplete() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))
        val reservation = Reservation.of(testMovie(), testTheater())

        controller.moveToReservationComplete(reservation)

        assertThat(controller.screen()).isEqualTo(Screen.ReservationComplete(reservation))
    }

    @Test
    @DisplayName("예매 상세 화면으로 이동하면 예매 정보가 담긴 화면으로 바뀐다")
    fun moveToReservationDetail() {
        val controller = MovieNavigationController(mutableStateOf(Screen.Tab.Home))
        val reservation = Reservation.of(testMovie(), testTheater())

        controller.moveToReservationDetail(reservation)

        assertThat(controller.screen()).isEqualTo(Screen.ReservationDetail(reservation))
    }
}
