package com.example.movietiket.fixture

import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieDescription
import com.example.movietiket.model.MovieTitle
import com.example.movietiket.model.RunningTime
import com.example.movietiket.model.Screening
import com.example.movietiket.model.ScreeningPeriod
import com.example.movietiket.model.Synopsis
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
