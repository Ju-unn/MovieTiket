package com.example.movietiket.common.model.theater

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 극장 목록 컬렉션의 생성 제약과 조회 동작을 검증한다.
 */
class TheatersTest {

    // 빈 목록으로는 Theaters를 생성할 수 없음을 검증
    @Test
    @DisplayName("극장 목록은 비어 있을 수 없다")
    fun cannotCreateEmptyTheaters() {
        assertThatIllegalArgumentException()
            .isThrownBy { Theaters(emptyList()) }
    }

    // 생성 시 전달한 극장 목록이 그대로 조회되는지 검증
    @Test
    @DisplayName("생성 시 전달한 극장 목록을 그대로 조회할 수 있다")
    fun toList() {
        val theater = Theater(id = 0, name = TheaterName("강남점"))

        val theaters = Theaters(listOf(theater))

        assertThat(theaters.toList()).containsExactly(theater)
    }
}
