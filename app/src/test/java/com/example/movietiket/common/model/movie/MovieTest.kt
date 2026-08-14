package com.example.movietiket.common.model.movie

import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieTest {

    private fun movie(): Movie {
        return Movie(
            id = 1,
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152)),
        )
    }

    @Test
    @DisplayName("영화 식별자를 조회한다")
    fun id() {
        assertThat(movie().id()).isEqualTo(1)
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
    @DisplayName("상영 기간을 표시용 값으로 조회한다")
    fun displayScreeningPeriod() {
        assertThat(movie().displayScreeningPeriod()).isEqualTo("2024.3.1 ~ 2024.3.28")
    }

    @Test
    @DisplayName("러닝타임을 분 단위로 조회한다")
    fun runningTimeMinutes() {
        assertThat(movie().runningTimeMinutes()).isEqualTo(152)
    }
}
