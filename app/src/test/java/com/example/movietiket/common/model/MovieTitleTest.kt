package com.example.movietiket.common.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieTitleTest {

    @Test
    @DisplayName("영화 제목을 생성할 수 있다")
    fun createValidTitle() {
        val title = MovieTitle("해리 포터와 마법사의 돌")

        assertThat(title.toDisplayValue()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("빈 영화 제목은 생성할 수 없다")
    fun cannotCreateBlankTitle() {
        assertThatIllegalArgumentException()
            .isThrownBy { MovieTitle("   ") }
    }
}
