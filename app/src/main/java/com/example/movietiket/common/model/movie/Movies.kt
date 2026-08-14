package com.example.movietiket.common.model.movie

/**
 * 영화 목록 일급 컬렉션
 */
class Movies(private val movies: List<Movie>) {
    init {
        require(movies.isNotEmpty()) { "영화 목록은 비어 있을 수 없다" }
    }

    fun toList(): List<Movie> = movies.toList()
}
