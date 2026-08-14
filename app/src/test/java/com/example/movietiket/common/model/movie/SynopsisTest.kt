package com.example.movietiket.common.model.movie

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SynopsisTest {

    @Test
    @DisplayName("영화 소개를 생성할 수 있다")
    fun createValidSynopsis() {
        val synopsis = Synopsis("평범한 소년 해리 포터가 호그와트 마법 학교에 입학하며 겪는 모험을 그린다.")

        assertThat(synopsis.toDisplayValue()).isEqualTo("평범한 소년 해리 포터가 호그와트 마법 학교에 입학하며 겪는 모험을 그린다.")
    }

    @Test
    @DisplayName("빈 영화 소개는 생성할 수 없다")
    fun cannotCreateBlankSynopsis() {
        assertThatIllegalArgumentException()
            .isThrownBy { Synopsis("   ") }
    }
}
