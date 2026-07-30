package com.example.movietiket.presenter

import com.example.movietiket.model.Movie

/**
 * 영화 목록 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface MovieListContract {

    interface View {
        fun showMovies(movies: List<Movie>)
    }

    interface Presenter {
        fun loadMovies()
        fun onReserveClick(movie: Movie)
    }
}
