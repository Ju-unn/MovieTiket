package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.movie.MovieDescription
import com.example.movietiket.common.model.movie.Movies
import com.example.movietiket.common.model.movie.MovieTitle
import com.example.movietiket.common.model.movie.RunningTime
import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import com.example.movietiket.common.model.movie.Synopsis
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.model.theater.TheaterName
import com.example.movietiket.common.model.theater.Theaters
import com.example.movietiket.movielist.model.MovieListRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * 영화 목록 프레젠터의 영화/광고 표시, 극장 선택, 예매 요청 콜백 흐름을 검증한다.
 */
class MovieListPresenterTest {

    // 테스트용 영화 생성
    private fun movie(): Movie = Movie(
        id = 1,
        description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
        screening = Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152)),
    )

    // 테스트용 극장 생성
    private fun theater(): Theater = Theater(id = 1, name = TheaterName("강남점"))

    // 테스트용 View 구현체: 전달받은 영화 행/극장 목록을 저장해 검증에 사용
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

    // 영화 목록을 불러오면 View에 전체 목록이 전달되는지 검증
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

    // 영화 3개마다 광고가 끼워진 목록이 View에 전달되는지 검증
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

    // 예매 버튼 클릭 시 극장 선택 목록만 표시되고 예매로는 넘어가지 않는지 검증
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

    // 극장 선택 시 선택된 영화/극장으로 콜백이 호출되는지 검증
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

    // 극장 선택 취소 시 예매로 넘어가지 않는지 검증
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

    // 예매 버튼을 거치지 않고 극장만 선택하면 콜백이 호출되지 않는지 검증
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
