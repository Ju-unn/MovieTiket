package com.example.movietiket.common.model.theater

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 극장 이름 값 객체의 생성 및 유효성 검증을 확인한다.
 */
class TheaterNameTest {

    // 정상적인 극장 이름 생성 및 표시값 조회 검증
    @Test
    @DisplayName("극장 이름을 생성할 수 있다")
    fun createValidName() {
        val name = TheaterName("강남점")

        assertThat(name.toDisplayValue()).isEqualTo("강남점")
    }

    // 빈 문자열로는 극장 이름을 생성할 수 없음을 검증
    @Test
    @DisplayName("빈 극장 이름은 생성할 수 없다")
    fun cannotCreateBlankName() {
        assertThatIllegalArgumentException()
            .isThrownBy { TheaterName("   ") }
    }
}
