package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.MovieDescription
import com.example.movietiket.common.model.Movies
import com.example.movietiket.common.model.MovieTitle
import com.example.movietiket.common.model.RunningTime
import com.example.movietiket.common.model.Screening
import com.example.movietiket.common.model.ScreeningPeriod
import com.example.movietiket.common.model.Synopsis
import com.example.movietiket.common.model.Theater
import com.example.movietiket.common.model.TheaterName
import com.example.movietiket.common.model.Theaters
import com.example.movietiket.movielist.model.MovieListRow
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

    private fun theater(): Theater = Theater(id = 1, name = TheaterName("강남점"))

    private class FakeView : MovieListContract.View {
        var shownRows: List<MovieListRow> = emptyList()
        var shownTheaters: List<Theater>? = null

        /** 검증 편의를 위해 광고를 뺀 영화만 뽑아 준다 */
        val shownMovies: List<Movie>
            get() = shownRows.filterIsInstance<MovieListRow.MovieRow>().map { it.movie }

        override fun showMovies(rows: List<MovieListRow>) {
            shownRows = rows
        }

        override fun showTheaterSelection(theaters: List<Theater>) {
            shownTheaters = theaters
        }

        override fun hideTheaterSelection() {
            shownTheaters = null
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
            theaters = Theaters(listOf(theater())),
            onMovieReserveRequested = { _, _ -> },
        )

        presenter.loadMovies()

        assertThat(view.shownMovies).containsExactly(targetMovie)
    }

    @Test
    @DisplayName("영화 3개마다 광고가 끼워진 목록을 View에 전달한다")
    fun insertsAdRows() {
        val view = FakeView()
        val fourMovies = (0 until 4).map { movie() }
        val presenter = MovieListPresenter(
            view = view,
            movies = Movies(fourMovies),
            theaters = Theaters(listOf(theater())),
            onMovieReserveRequested = { _, _ -> },
        )

        presenter.loadMovies()

        assertThat(view.shownRows.map { it is MovieListRow.AdRow })
            .containsExactly(false, false, false, true, false)
    }

    @Test
    @DisplayName("예매 버튼을 클릭하면 극장 선택 목록을 보여주고 아직 예매로 넘어가지 않는다")
    fun onReserveClickShowsTheaters() {
        val view = FakeView()
        val targetTheater = theater()
        var requested = false
        val presenter = MovieListPresenter(
            view = view,
            movies = Movies(listOf(movie())),
            theaters = Theaters(listOf(targetTheater)),
            onMovieReserveRequested = { _, _ -> requested = true },
        )

        presenter.onReserveClick(movie())

        assertThat(view.shownTheaters).containsExactly(targetTheater)
        assertThat(requested).isFalse()
    }

    @Test
    @DisplayName("극장을 선택하면 선택한 영화와 극장으로 콜백을 호출한다")
    fun onTheaterSelected() {
        val view = FakeView()
        val targetMovie = movie()
        val targetTheater = theater()
        var requestedMovie: Movie? = null
        var requestedTheater: Theater? = null
        val presenter = MovieListPresenter(
            view = view,
            movies = Movies(listOf(targetMovie)),
            theaters = Theaters(listOf(targetTheater)),
            onMovieReserveRequested = { m, t -> requestedMovie = m; requestedTheater = t },
        )
        presenter.onReserveClick(targetMovie)

        presenter.onTheaterSelected(targetTheater)

        assertThat(requestedMovie).isSameAs(targetMovie)
        assertThat(requestedTheater).isSameAs(targetTheater)
        assertThat(view.shownTheaters).isNull()
    }

    @Test
    @DisplayName("극장 선택을 닫으면 예매로 넘어가지 않는다")
    fun onTheaterSelectionDismissed() {
        val view = FakeView()
        var requested = false
        val presenter = MovieListPresenter(
            view = view,
            movies = Movies(listOf(movie())),
            theaters = Theaters(listOf(theater())),
            onMovieReserveRequested = { _, _ -> requested = true },
        )
        presenter.onReserveClick(movie())

        presenter.onTheaterSelectionDismissed()

        assertThat(view.shownTheaters).isNull()
        assertThat(requested).isFalse()
    }

    @Test
    @DisplayName("예매 버튼을 거치지 않고 극장만 선택하면 아무 일도 일어나지 않는다")
    fun theaterSelectedWithoutReserveClick() {
        var requested = false
        val presenter = MovieListPresenter(
            view = FakeView(),
            movies = Movies(listOf(movie())),
            theaters = Theaters(listOf(theater())),
            onMovieReserveRequested = { _, _ -> requested = true },
        )

        presenter.onTheaterSelected(theater())

        assertThat(requested).isFalse()
    }
}
