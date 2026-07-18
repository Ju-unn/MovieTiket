package com.example.movietiket.model

/**
 * 영화의 제목과 소개를 묶은 객체
 */
class MovieDescription(
    private val title: MovieTitle,
    private val synopsis: Synopsis,
) {
    fun displayTitle(): String = title.toDisplayValue()

    fun displaySynopsis(): String = synopsis.toDisplayValue()
}
