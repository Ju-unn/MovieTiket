package com.example.movietiket.common.repository

import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.model.theater.TheaterName
import com.example.movietiket.common.model.theater.Theaters

/**
 * 극장 목록 데이터를 제공하는 저장소
 * (미션 특성상 고정 데이터를 메모리에서 제공한다)
 */
object TheaterRepository {

    // 전체 극장 목록을 반환한다
    fun findAll(): Theaters = THEATERS

    // id에 해당하는 극장을 조회한다
    fun findById(id: Int): Theater = findAll().toList().first { it.id() == id }

    private val THEATERS: Theaters = Theaters(listOf(
        theaterOf(id = 0, name = "강남점"),
        theaterOf(id = 1, name = "왕십리점"),
        // 이름이 화면 폭을 넘길 때 말줄임 처리를 확인하기 위한 극장
        theaterOf(id = 2, name = "너무너무너무긴 극장이름을 가진 코엑스점"),
    ))

    // id와 이름으로 극장 객체를 생성한다
    private fun theaterOf(id: Int, name: String): Theater = Theater(id, TheaterName(name))
}
