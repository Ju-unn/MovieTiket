package com.example.movietiket.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieRepositoryTest {

    @Test
    @DisplayName("저장소는 기본 영화 목록을 제공한다")
    fun findAll() {
        val movies = MovieRepository.findAll()

        assertThat(movies.toList()).hasSize(5)
    }

    @Test
    @DisplayName("id로 영화를 조회할 수 있다")
    fun findById() {
        val movie = MovieRepository.findById(0)

        assertThat(movie.displayTitle()).isEqualTo("해리 포터와 마법사의 돌")
    }

    @Test
    @DisplayName("각 영화는 28일간의 상영 기간을 가진다")
    fun screeningPeriodIsFourWeeks() {
        val movie = MovieRepository.findById(0)

        assertThat(movie.availableScreeningDates()).hasSize(28)
    }
}
