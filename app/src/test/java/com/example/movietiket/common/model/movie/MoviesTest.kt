package com.example.movietiket.common.model.movie

import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * Movies 컬렉션 값 객체의 생성 규칙을 검증하는 테스트
 */
class MoviesTest {

    // 빈 목록으로 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("영화 목록은 비어 있을 수 없다")
    fun cannotCreateEmptyMovies() {
        assertThatIllegalArgumentException()
            .isThrownBy { Movies(emptyList()) }
    }

    // 생성 시 전달한 목록이 그대로 조회되는지 검증
    @Test
    @DisplayName("생성 시 전달한 영화 목록을 그대로 조회할 수 있다")
    fun toList() {
        val movie = Movie(
            id = 1,
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152)),
        )

        val movies = Movies(listOf(movie))

        assertThat(movies.toList()).containsExactly(movie)
    }
}
