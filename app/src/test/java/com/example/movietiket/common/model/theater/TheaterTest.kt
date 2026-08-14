package com.example.movietiket.common.model.theater

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TheaterTest {

    @Test
    @DisplayName("극장 이름을 표시용 값으로 조회할 수 있다")
    fun displayName() {
        val theater = Theater(id = 0, name = TheaterName("강남점"))

        assertThat(theater.displayName()).isEqualTo("강남점")
    }

    @Test
    @DisplayName("빈 극장 이름은 생성할 수 없다")
    fun cannotCreateBlankName() {
        assertThatIllegalArgumentException()
            .isThrownBy { TheaterName("   ") }
    }

    @Test
    @DisplayName("빈 극장 목록은 생성할 수 없다")
    fun cannotCreateEmptyTheaters() {
        assertThatIllegalArgumentException()
            .isThrownBy { Theaters(emptyList()) }
    }
}
