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
}
