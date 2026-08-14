package com.example.movietiket.common.model.movie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * MovieDescription의 표시용 값 조회를 검증하는 테스트
 */
class MovieDescriptionTest {

    // 테스트용 영화 설명 생성
    private fun description(): MovieDescription {
        return MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개"))
    }

    // 제목이 표시용 값으로 정상 조회되는지 검증
    @Test
    @DisplayName("영화 제목을 표시용 값으로 조회한다")
    fun displayTitle() {
        assertThat(description().displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    // 소개가 표시용 값으로 정상 조회되는지 검증
    @Test
    @DisplayName("영화 소개를 표시용 값으로 조회한다")
    fun displaySynopsis() {
        assertThat(description().displaySynopsis()).isEqualTo("소개")
    }
}
