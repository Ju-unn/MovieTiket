package com.example.movietiket.presenter

import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieDescription
import com.example.movietiket.model.MovieTitle
import com.example.movietiket.model.Reservation
import com.example.movietiket.model.RunningTime
import com.example.movietiket.model.Screening
import com.example.movietiket.model.ScreeningDate
import com.example.movietiket.model.Synopsis
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ReservationPresenterTest {

    private fun movie(): Movie = Movie(
        description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
        screening = Screening(ScreeningDate("2024.3.1"), RunningTime(152)),
    )

    private class FakeView : ReservationContract.View {
        lateinit var shownReservation: Reservation
        override fun showReservation(reservation: Reservation) {
            shownReservation = reservation
        }
    }

    @Test
    @DisplayName("생성 시 최소 인원 1명인 예매를 View에 통지한다")
    fun initialShowsMinimumHeadCount() {
        val view = FakeView()

        ReservationPresenter(movie = movie(), view = view, onReservationConfirmed = {})

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("1")
    }

    @Test
    @DisplayName("인원 증가 시 View에 증가한 인원을 통지한다")
    fun increaseHeadCount() {
        val view = FakeView()
        val presenter = ReservationPresenter(movie = movie(), view = view, onReservationConfirmed = {})

        presenter.increaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("인원 감소 시 View에 감소한 인원을 통지한다")
    fun decreaseHeadCount() {
        val view = FakeView()
        val presenter = ReservationPresenter(movie = movie(), view = view, onReservationConfirmed = {})

        presenter.increaseHeadCount()
        presenter.increaseHeadCount()
        presenter.decreaseHeadCount()

        assertThat(view.shownReservation.displayHeadCount()).isEqualTo("2")
    }

    @Test
    @DisplayName("예매 확정 시 현재 예매 내용으로 콜백을 호출한다")
    fun confirmReservation() {
        var confirmed: Reservation? = null
        val presenter = ReservationPresenter(
            movie = movie(),
            view = FakeView(),
            onReservationConfirmed = { confirmed = it },
        )

        presenter.increaseHeadCount()
        presenter.confirmReservation()

        assertThat(confirmed?.displayHeadCount()).isEqualTo("2")
    }
}
