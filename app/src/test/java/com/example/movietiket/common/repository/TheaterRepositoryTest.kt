package com.example.movietiket.common.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 극장 저장소가 제공하는 기본 극장 목록과 조회 기능을 검증한다.
 */
class TheaterRepositoryTest {

    // 극장 목록 조회 시 3개의 극장이 반환되는지 검증
    @Test
    @DisplayName("극장 목록을 조회하면 3개의 극장을 반환한다")
    fun findAll() {
        val theaters = TheaterRepository.findAll().toList()

        assertThat(theaters).hasSize(3)
        assertThat(theaters.map { it.displayName() }).containsExactly(
            "강남점",
            "왕십리점",
            "너무너무너무긴 극장이름을 가진 코엑스점",
        )
    }

    // id로 극장을 조회할 수 있는지 검증
    @Test
    @DisplayName("id로 극장을 조회할 수 있다")
    fun findById() {
        assertThat(TheaterRepository.findById(1).displayName()).isEqualTo("왕십리점")
    }
}
