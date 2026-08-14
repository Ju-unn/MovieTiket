package com.example.movietiket.common.model.theater

/**
 * 영화를 상영하는 극장 한 곳을 표현하는 도메인 객체
 */
class Theater(
    private val id: Int,
    private val name: TheaterName,
) {
    fun id(): Int = id

    // 화면 표시용 극장 이름을 반환한다
    fun displayName(): String = name.toDisplayValue()
}
