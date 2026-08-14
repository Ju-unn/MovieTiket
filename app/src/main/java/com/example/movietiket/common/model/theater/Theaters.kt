package com.example.movietiket.common.model.theater

/**
 * 극장 목록 일급 컬렉션
 */
class Theaters(private val theaters: List<Theater>) {
    init {
        require(theaters.isNotEmpty()) { "극장 목록은 비어 있을 수 없다" }
    }

    // 극장 목록을 리스트로 반환한다
    fun toList(): List<Theater> = theaters.toList()
}
