package com.example.movietiket.fixture

import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieDescription
import com.example.movietiket.model.MovieTitle
import com.example.movietiket.model.RunningTime
import com.example.movietiket.model.Screening
import com.example.movietiket.model.ScreeningDate
import com.example.movietiket.model.Synopsis

/**
 * test / androidTest에서 공통으로 사용하는 테스트용 영화 픽스처
 */
fun testMovie(): Movie = Movie(
    description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
    screening = Screening(ScreeningDate("2024.3.1"), RunningTime(152)),
)
