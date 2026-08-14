package com.example.movietiket.common.model.movie

import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * Movie의 식별자/표시용 값 조회를 검증하는 테스트
 */
class MovieTest {

    // 테스트용 영화 생성
    private fun movie(): Movie {
        return Movie(
            id = 1,
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152)),
        )
    }

    // 영화 식별자가 정상 조회되는지 검증
    @Test
    @DisplayName("영화 식별자를 조회한다")
    fun id() {
        assertThat(movie().id()).isEqualTo(1)
    }

    // 제목이 표시용 값으로 정상 조회되는지 검증
    @Test
    @DisplayName("영화 제목을 표시용 값으로 조회한다")
    fun displayTitle() {
        assertThat(movie().displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    // 소개가 표시용 값으로 정상 조회되는지 검증
    @Test
    @DisplayName("영화 소개를 표시용 값으로 조회한다")
    fun displaySynopsis() {
        assertThat(movie().displaySynopsis()).isEqualTo("소개")
    }

    // 상영 기간이 표시용 값으로 정상 조회되는지 검증
    @Test
    @DisplayName("상영 기간을 표시용 값으로 조회한다")
    fun displayScreeningPeriod() {
        assertThat(movie().displayScreeningPeriod()).isEqualTo("2024.3.1 ~ 2024.3.28")
    }

    // 러닝타임이 분 단위로 정상 조회되는지 검증
    @Test
    @DisplayName("러닝타임을 분 단위로 조회한다")
    fun runningTimeMinutes() {
        assertThat(movie().runningTimeMinutes()).isEqualTo(152)
    }
}
