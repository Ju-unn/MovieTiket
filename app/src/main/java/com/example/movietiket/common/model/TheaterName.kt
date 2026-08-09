package com.example.movietiket.common.model

/**
 * 극장 이름을 포장한 값 객체
 */
@JvmInline
value class TheaterName(private val value: String) {
    init {
        require(value.isNotBlank()) { "극장 이름은 비어 있을 수 없다" }
    }

    fun toDisplayValue(): String = value
}
