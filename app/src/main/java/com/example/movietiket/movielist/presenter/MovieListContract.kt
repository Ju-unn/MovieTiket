package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.movielist.model.MovieListRow

/**
 * 영화 목록 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface MovieListContract {

    interface View {
        /** 광고가 끼워진 상태의 목록을 그대로 그린다 */
        fun showMovies(rows: List<MovieListRow>)

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
