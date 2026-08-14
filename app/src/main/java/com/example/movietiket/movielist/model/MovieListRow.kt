package com.example.movietiket.movielist.model

import com.example.movietiket.common.model.movie.Movie

/**
 * 영화 목록에 실제로 그려지는 한 줄
 * 영화 사이에 광고가 끼어들므로 View가 "몇 번째마다 광고"인지 몰라도 되게 한다
 */
sealed interface MovieListRow {

    data class MovieRow(val movie: Movie) : MovieListRow

    data object AdRow : MovieListRow

    companion object {
        /** 영화 3개마다 광고를 한 번 넣는다 */
        private const val AD_INTERVAL = 3

        // 영화 목록에 광고 행을 끼워 넣어 화면에 그릴 목록을 만든다
        fun of(movies: List<Movie>): List<MovieListRow> = buildList {
            movies.forEachIndexed { index, movie ->
                add(MovieRow(movie))
                if (needsAdAfter(index, movies.lastIndex)) add(AdRow)
            }
        }

        // 마지막 영화 뒤에는 광고를 붙이지 않는다 (목록이 광고로 끝나면 어색하다)
        private fun needsAdAfter(index: Int, lastIndex: Int): Boolean =
            (index + 1) % AD_INTERVAL == 0 && index != lastIndex
    }
}
