package com.example.movietiket.common.model

/**
 * 영화를 상영하는 극장 한 곳을 표현하는 도메인 객체
 */
class Theater(
    private val id: Int,
    private val name: TheaterName,
) {
    fun id(): Int = id

    fun displayName(): String = name.toDisplayValue()
}
