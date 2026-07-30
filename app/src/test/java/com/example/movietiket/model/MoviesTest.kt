package com.example.movietiket.model

import com.example.movietiket.repository.MovieRepository
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
    @DisplayName("저장소는 기본 영화 목록을 제공한다")
    fun repositoryProvidesDefaultMovies() {
        val movies = MovieRepository.findAll()

        assertThat(movies.toList()).hasSize(5)
    }

    @Test
    @DisplayName("영화는 제목/상영일/러닝타임을 표시용 값으로 제공한다")
    fun movieDisplayValues() {
        val movie = MovieRepository.findAll()
            .toList()
            .first()

        assertThat(movie.displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
        assertThat(movie.displayScreeningDate()).isEqualTo("2024.3.1")
        assertThat(movie.runningTimeMinutes()).isEqualTo(152)
    }
}
