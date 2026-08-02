package com.example.movietiket.presenter

import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieDescription
import com.example.movietiket.model.Movies
import com.example.movietiket.model.MovieTitle
import com.example.movietiket.model.RunningTime
import com.example.movietiket.model.Screening
import com.example.movietiket.model.ScreeningPeriod
import com.example.movietiket.model.Synopsis
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieListPresenterTest {

    private fun movie(): Movie = Movie(
        id = 1,
        description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
        screening = Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152)),
    )

    private class FakeView : MovieListContract.View {
        var shownMovies: List<Movie> = emptyList()
        override fun showMovies(movies: List<Movie>) {
            shownMovies = movies
        }
    }

    @Test
    @DisplayName("영화 목록을 불러오면 View에 전체 목록을 전달한다")
    fun loadMovies() {
        val view = FakeView()
        val targetMovie = movie()
        val presenter = MovieListPresenter(
            view = view,
            movies = Movies(listOf(targetMovie)),
            onMovieReserveRequested = {},
        )

        presenter.loadMovies()

        assertThat(view.shownMovies).containsExactly(targetMovie)
    }

    @Test
    @DisplayName("예매 버튼 클릭 시 선택한 영화로 콜백을 호출한다")
    fun onReserveClick() {
        val targetMovie = movie()
        var requestedMovie: Movie? = null
        val presenter = MovieListPresenter(
            view = FakeView(),
            movies = Movies(listOf(targetMovie)),
            onMovieReserveRequested = { requestedMovie = it },
        )

        presenter.onReserveClick(targetMovie)

        assertThat(requestedMovie).isSameAs(targetMovie)
    }
}
