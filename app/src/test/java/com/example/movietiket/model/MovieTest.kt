package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieTest {

    private fun movie(): Movie {
        return Movie(
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningDate("2024.3.1"), RunningTime(152)),
        )
    }

    @Test
    @DisplayName("영화 제목을 표시용 값으로 조회한다")
    fun displayTitle() {
        assertThat(movie().displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("영화 소개를 표시용 값으로 조회한다")
    fun displaySynopsis() {
        assertThat(movie().displaySynopsis()).isEqualTo("소개")
    }

    @Test
    @DisplayName("상영일을 표시용 값으로 조회한다")
    fun displayScreeningDate() {
        assertThat(movie().displayScreeningDate()).isEqualTo("2024.3.1")
    }

    @Test
    @DisplayName("러닝타임을 분 단위로 조회한다")
    fun runningTimeMinutes() {
        assertThat(movie().runningTimeMinutes()).isEqualTo(152)
    }
}
