package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Movies
import com.example.movietiket.common.model.Theater
import com.example.movietiket.common.model.Theaters
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository

/**
 * 영화 목록 화면의 흐름을 제어하는 Presenter
 * 영화/극장 목록은 Repository에서 직접 불러온다 (View는 데이터 출처를 모른다)
 * "지금 예매" -> 극장 선택 -> 예매 화면 순서로 흐름을 이어준다
 */
class MovieListPresenter(
    private val view: MovieListContract.View,
    private val movies: Movies = MovieRepository.findAll(),
    private val theaters: Theaters = TheaterRepository.findAll(),
    private val onMovieReserveRequested: (Movie, Theater) -> Unit,
) : MovieListContract.Presenter {

    // 극장을 고르는 동안 어떤 영화를 예매하려던 것인지 기억해 둔다
    private var movieBeingReserved: Movie? = null

    init {
        loadMovies()
    }

    override fun loadMovies() {
        view.showMovies(movies.toList())
    }

    override fun onReserveClick(movie: Movie) {
        movieBeingReserved = movie
        view.showTheaterSelection(theaters.toList())
    }

    override fun onTheaterSelected(theater: Theater) {
        val movie = movieBeingReserved ?: return
        movieBeingReserved = null
        view.hideTheaterSelection()
        onMovieReserveRequested(movie, theater)
    }

    override fun onTheaterSelectionDismissed() {
        movieBeingReserved = null
        view.hideTheaterSelection()
    }
}
