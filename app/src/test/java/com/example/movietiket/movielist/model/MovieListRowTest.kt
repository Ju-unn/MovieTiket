package com.example.movietiket.movielist.model

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.MovieDescription
import com.example.movietiket.common.model.MovieTitle
import com.example.movietiket.common.model.RunningTime
import com.example.movietiket.common.model.Screening
import com.example.movietiket.common.model.ScreeningPeriod
import com.example.movietiket.common.model.Synopsis
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieListRowTest {

    private fun movies(count: Int): List<Movie> = (0 until count).map { id ->
        Movie(
            id = id,
            description = MovieDescription(MovieTitle("영화 $id"), Synopsis("소개")),
            screening = Screening(
                ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)),
                RunningTime(120),
            ),
        )
    }

    private fun kinds(rows: List<MovieListRow>): List<String> =
        rows.map { if (it is MovieListRow.AdRow) "광고" else "영화" }

    @Test
    @DisplayName("영화가 3개보다 적으면 광고가 들어가지 않는다")
    fun fewerThanInterval() {
        assertThat(kinds(MovieListRow.of(movies(2)))).containsExactly("영화", "영화")
    }

    @Test
    @DisplayName("영화가 정확히 3개면 마지막 뒤에는 광고를 붙이지 않는다")
    fun exactlyInterval() {
        assertThat(kinds(MovieListRow.of(movies(3)))).containsExactly("영화", "영화", "영화")
    }

    @Test
    @DisplayName("영화 3개 뒤에 광고가 한 번 들어간다")
    fun adAfterEveryThird() {
        assertThat(kinds(MovieListRow.of(movies(4))))
            .containsExactly("영화", "영화", "영화", "광고", "영화")
    }

    @Test
    @DisplayName("영화가 7개면 3번째와 6번째 뒤에 광고가 들어간다")
    fun adRepeatsEveryThird() {
        assertThat(kinds(MovieListRow.of(movies(7)))).containsExactly(
            "영화", "영화", "영화", "광고",
            "영화", "영화", "영화", "광고",
            "영화",
        )
    }

    @Test
    @DisplayName("영화가 6개면 마지막이 3의 배수라 광고로 끝나지 않는다")
    fun doesNotEndWithAd() {
        val rows = MovieListRow.of(movies(6))

        assertThat(rows.last()).isInstanceOf(MovieListRow.MovieRow::class.java)
        assertThat(kinds(rows)).containsExactly(
            "영화", "영화", "영화", "광고",
            "영화", "영화", "영화",
        )
    }

    @Test
    @DisplayName("영화 목록이 비어 있으면 아무 줄도 만들지 않는다")
    fun emptyMovies() {
        assertThat(MovieListRow.of(emptyList())).isEmpty()
    }
}
