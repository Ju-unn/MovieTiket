package com.example.movietiket.movielist.model

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.movie.MovieDescription
import com.example.movietiket.common.model.movie.MovieTitle
import com.example.movietiket.common.model.movie.RunningTime
import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import com.example.movietiket.common.model.movie.Synopsis
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * 영화 목록에 3개 간격으로 광고 행이 삽입되는 규칙을 검증한다.
 */
class MovieListRowTest {

    // 테스트용 영화 목록 생성
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

    // 행 목록을 광고/영화 종류 문자열 목록으로 변환
    private fun kinds(rows: List<MovieListRow>): List<String> =
        rows.map { if (it is MovieListRow.AdRow) "광고" else "영화" }

    // 영화가 3개보다 적으면 광고가 삽입되지 않는지 검증
    @Test
    @DisplayName("영화가 3개보다 적으면 광고가 들어가지 않는다")
    fun fewerThanInterval() {
        assertThat(kinds(MovieListRow.of(movies(2)))).containsExactly("영화", "영화")
    }

    // 영화가 정확히 3개면 마지막 뒤에 광고가 붙지 않는지 검증
    @Test
    @DisplayName("영화가 정확히 3개면 마지막 뒤에는 광고를 붙이지 않는다")
    fun exactlyInterval() {
        assertThat(kinds(MovieListRow.of(movies(3)))).containsExactly("영화", "영화", "영화")
    }

    // 영화 3개마다 광고가 한 번씩 삽입되는지 검증
    @Test
    @DisplayName("영화 3개 뒤에 광고가 한 번 들어간다")
    fun adAfterEveryThird() {
        assertThat(kinds(MovieListRow.of(movies(4))))
            .containsExactly("영화", "영화", "영화", "광고", "영화")
    }

    // 영화가 7개일 때 3번째, 6번째 뒤에 광고가 반복 삽입되는지 검증
    @Test
    @DisplayName("영화가 7개면 3번째와 6번째 뒤에 광고가 들어간다")
    fun adRepeatsEveryThird() {
        assertThat(kinds(MovieListRow.of(movies(7)))).containsExactly(
            "영화", "영화", "영화", "광고",
            "영화", "영화", "영화", "광고",
            "영화",
        )
    }

    // 영화가 6개(3의 배수)면 마지막이 광고로 끝나지 않는지 검증
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

    // 영화 목록이 비어 있으면 행이 생성되지 않는지 검증
    @Test
    @DisplayName("영화 목록이 비어 있으면 아무 줄도 만들지 않는다")
    fun emptyMovies() {
        assertThat(MovieListRow.of(emptyList())).isEmpty()
    }
}
