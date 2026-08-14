package com.example.movietiket.common.model.theater

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TheaterNameTest {

    @Test
    @DisplayName("극장 이름을 생성할 수 있다")
    fun createValidName() {
        val name = TheaterName("강남점")

        assertThat(name.toDisplayValue()).isEqualTo("강남점")
    }

    @Test
    @DisplayName("빈 극장 이름은 생성할 수 없다")
    fun cannotCreateBlankName() {
        assertThatIllegalArgumentException()
            .isThrownBy { TheaterName("   ") }
    }
}
