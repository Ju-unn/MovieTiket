package com.example.movietiket.presenter

import com.example.movietiket.model.Movie
import com.example.movietiket.model.Movies
import com.example.movietiket.repository.MovieRepository

/**
 * 영화 목록 화면의 흐름을 제어하는 Presenter
 */
class MovieListPresenter(
    private val view: MovieListContract.View,
    private val movies: Movies = MovieRepository.findAll(),
    private val onMovieReserveRequested: (Movie) -> Unit,
) : MovieListContract.Presenter {

    override fun loadMovies() {
        view.showMovies(movies.toList())
    }

    override fun onReserveClick(movie: Movie) {
        onMovieReserveRequested(movie)
    }
}
