package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.movie.MovieDescription
import com.example.movietiket.common.model.movie.MovieTitle
import com.example.movietiket.common.model.movie.RunningTime
import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import com.example.movietiket.common.model.movie.Synopsis
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.model.theater.TheaterName
import java.time.LocalDate

// 2024.3.1(금요일, 평일)~2024.3.28
private val TEST_START_DATE: LocalDate = LocalDate.of(2024, 3, 1)
private val TEST_END_DATE: LocalDate = TEST_START_DATE.plusDays(27)

/**
 * test / androidTest에서 공통으로 사용하는 테스트용 영화 픽스처
 */
fun testMovie(): Movie = Movie(
    id = 0,
    description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
    screening = Screening(ScreeningPeriod(TEST_START_DATE, TEST_END_DATE), RunningTime(152)),
)

fun testTheater(): Theater = Theater(id = 0, name = TheaterName("강남점"))
