package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieDescriptionTest {

    private fun description(): MovieDescription {
        return MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개"))
    }

    @Test
    @DisplayName("영화 제목을 표시용 값으로 조회한다")
    fun displayTitle() {
        assertThat(description().displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("영화 소개를 표시용 값으로 조회한다")
    fun displaySynopsis() {
        assertThat(description().displaySynopsis()).isEqualTo("소개")
    }
}
