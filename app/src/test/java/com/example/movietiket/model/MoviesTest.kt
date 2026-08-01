package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MoviesTest {

    @Test
    @DisplayName("영화 목록은 비어 있을 수 없다")
    fun cannotCreateEmptyMovies() {
        assertThatIllegalArgumentException()
            .isThrownBy { Movies(emptyList()) }
    }

    @Test
    @DisplayName("생성 시 전달한 영화 목록을 그대로 조회할 수 있다")
    fun toList() {
        val movie = Movie(
            description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
            screening = Screening(ScreeningDate("2024.3.1"), RunningTime(152)),
        )

        val movies = Movies(listOf(movie))

        assertThat(movies.toList()).containsExactly(movie)
    }
}
