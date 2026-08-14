package com.example.movietiket.common.model.theater

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TheatersTest {

    @Test
    @DisplayName("극장 목록은 비어 있을 수 없다")
    fun cannotCreateEmptyTheaters() {
        assertThatIllegalArgumentException()
            .isThrownBy { Theaters(emptyList()) }
    }

    @Test
    @DisplayName("생성 시 전달한 극장 목록을 그대로 조회할 수 있다")
    fun toList() {
        val theater = Theater(id = 0, name = TheaterName("강남점"))

        val theaters = Theaters(listOf(theater))

        assertThat(theaters.toList()).containsExactly(theater)
    }
}
