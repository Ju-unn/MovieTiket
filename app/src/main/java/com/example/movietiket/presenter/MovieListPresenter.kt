package com.example.movietiket.presenter

import com.example.movietiket.model.Movie
import com.example.movietiket.model.Movies
import com.example.movietiket.repository.MovieRepository

/**
 * 영화 목록 화면의 흐름을 제어하는 Presenter
 * 영화 목록은 Repository에서 직접 불러온다 (View는 데이터 출처를 모른다)
 */
class MovieListPresenter(
    private val view: MovieListContract.View,
    private val movies: Movies = MovieRepository.findAll(),
    private val onMovieReserveRequested: (Movie) -> Unit,
) : MovieListContract.Presenter {

    init {
        loadMovies()
    }

    override fun loadMovies() {
        view.showMovies(movies.toList())
    }

    override fun onReserveClick(movie: Movie) {
        onMovieReserveRequested(movie)
    }
}
