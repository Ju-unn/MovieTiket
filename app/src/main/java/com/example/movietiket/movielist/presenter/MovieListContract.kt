package com.example.movietiket.movielist.presenter

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.movielist.model.MovieListRow

/**
 * 영화 목록 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface MovieListContract {

    /** 영화 목록 화면에 데이터를 표시하는 View */
    interface View {
        /** 광고가 끼워진 상태의 목록을 그대로 그린다 */
        fun showMovies(rows: List<MovieListRow>)

        /** "지금 예매"를 누르면 극장을 고르는 바텀시트를 띄운다 */
        fun showTheaterSelection(theaters: List<Theater>)

        // 극장 선택 바텀시트를 닫는다
        fun hideTheaterSelection()
    }

    /** 영화 목록 화면의 동작을 정의하는 Presenter */
    interface Presenter {
        // 영화 목록을 불러온다
        fun loadMovies()
        // "지금 예매" 클릭을 처리한다
        fun onReserveClick(movie: Movie)
        // 극장 선택을 처리한다
        fun onTheaterSelected(theater: Theater)
        // 극장 선택 취소를 처리한다
        fun onTheaterSelectionDismissed()
    }
}
