package com.example.movietiket.model

/**
 * 영화 제목을 포장한 값 객체
 */
@JvmInline
value class MovieTitle(private val value: String) {
    init {
        require(value.isNotBlank()) { "영화 제목은 비어 있을 수 없다" }
    }

    fun toDisplayValue(): String = value
}
