package com.example.movietiket.common.repository

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.MovieDescription
import com.example.movietiket.common.model.Movies
import com.example.movietiket.common.model.MovieTitle
import com.example.movietiket.common.model.RunningTime
import com.example.movietiket.common.model.Screening
import com.example.movietiket.common.model.ScreeningPeriod
import com.example.movietiket.common.model.Synopsis
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 영화 목록 데이터를 제공하는 저장소
 * (미션 특성상 고정 데이터를 메모리에서 제공한다)
 */
object MovieRepository {

    fun findAll(): Movies = HARRY_POTTER_SERIES

    fun findById(id: Int): Movie = findAll().toList().first { it.id() == id }

    private const val SCREENING_PERIOD_DAYS = 28L
    private val START_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.M.d")

    private val HARRY_POTTER_SERIES: Movies = Movies(listOf(
        movieOf(
            id = 0,
            title = "해리 포터와 마법사의 돌",
            synopsis = "《해리 포터와 마법사의 돌》은 J.K. 롤링의 동명 소설을 원작으로 한 영화이다. " +
                    "평범한 소년 해리 포터가 호그와트 마법 학교에 입학하며 겪는 모험을 그린다.",
            date = "2024.3.1",
            minutes = 152,
        ),
        movieOf(
            id = 1,
            title = "해리 포터와 비밀의 방",
            synopsis = "호그와트에 숨겨진 비밀의 방이 열리면서 벌어지는 사건을 그린 시리즈 두 번째 작품이다.",
            date = "2024.3.8",
            minutes = 161,
        ),
        movieOf(
            id = 2,
            title = "해리 포터와 아즈카반의 죄수",
            synopsis = "아즈카반 감옥을 탈출한 시리우스 블랙과 해리의 만남을 그린 시리즈 세 번째 작품이다.",
            date = "2024.3.15",
            minutes = 141,
        ),
        movieOf(
            id = 3,
            title = "해리 포터와 불의 잔",
            synopsis = "트리위저드 시합에 참가하게 된 해리의 이야기를 그린 시리즈 네 번째 작품이다.",
            date = "2024.3.22",
            minutes = 157,
        ),
        movieOf(
            id = 4,
            title = "해리 포터와 불사조 기사단",
            synopsis = "볼드모트의 부활을 알리려는 해리와 이를 부정하는 마법 세계의 갈등을 그린 다섯 번째 작품이다.",
            date = "2024.3.29",
            minutes = 138,
        ),
    ))

    private fun movieOf(id: Int, title: String, synopsis: String, date: String, minutes: Int): Movie {
        val startDate = LocalDate.parse(date, START_DATE_FORMATTER)
        val period = ScreeningPeriod(startDate, startDate.plusDays(SCREENING_PERIOD_DAYS - 1))
        return Movie(
            id = id,
            description = MovieDescription(MovieTitle(title), Synopsis(synopsis)),
            screening = Screening(period, RunningTime(minutes)),
        )
    }
}
