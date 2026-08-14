package com.example.movietiket.common.model.movie

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * MovieTitle 값 객체의 생성 규칙을 검증하는 테스트
 */
class MovieTitleTest {

    // 정상 문자열로 제목이 생성되는지 검증
    @Test
    @DisplayName("영화 제목을 생성할 수 있다")
    fun createValidTitle() {
        val title = MovieTitle("해리 포터와 마법사의 돌")

        assertThat(title.toDisplayValue()).isEqualTo("해리 포터와 마법사의 돌")
    }

    // 공백뿐인 제목은 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("빈 영화 제목은 생성할 수 없다")
    fun cannotCreateBlankTitle() {
        assertThatIllegalArgumentException()
            .isThrownBy { MovieTitle("   ") }
    }
}
