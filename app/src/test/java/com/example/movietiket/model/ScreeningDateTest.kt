package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ScreeningDateTest {

    @Test
    @DisplayName("yyyy.M.d 형식의 상영일을 생성할 수 있다")
    fun createValidDate() {
        val screeningDate = ScreeningDate("2024.3.1")

        assertThat(screeningDate.toDisplayValue()).isEqualTo("2024.3.1")
    }

    @Test
    @DisplayName("형식에 맞지 않는 상영일은 생성할 수 없다")
    fun cannotCreateInvalidFormat() {
        assertThatIllegalArgumentException()
            .isThrownBy { ScreeningDate("2024-03-01") }
    }
}
