package com.example.movietiket.model

/**
 * 영화 소개(줄거리)를 포장한 값 객체
 */
@JvmInline
value class Synopsis(private val value: String) {
    init {
        require(value.isNotBlank()) { "영화 소개는 비어 있을 수 없다" }
    }

    fun toDisplayValue(): String = value
}
