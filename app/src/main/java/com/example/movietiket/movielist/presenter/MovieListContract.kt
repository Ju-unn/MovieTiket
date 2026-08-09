package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Theater

/**
 * 영화 목록 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface MovieListContract {

    interface View {
        fun showMovies(movies: List<Movie>)

        /** "지금 예매"를 누르면 극장을 고르는 바텀시트를 띄운다 */
        fun showTheaterSelection(theaters: List<Theater>)

        fun hideTheaterSelection()
    }

    interface Presenter {
        fun loadMovies()
        fun onReserveClick(movie: Movie)
        fun onTheaterSelected(theater: Theater)
        fun onTheaterSelectionDismissed()
    }
}
