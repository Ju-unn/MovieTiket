package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.MovieDescription
import com.example.movietiket.common.model.MovieTitle
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.model.RunningTime
import com.example.movietiket.common.model.Screening
import com.example.movietiket.common.model.ScreeningPeriod
import com.example.movietiket.common.model.Synopsis
import java.time.LocalDate

// 2024.3.1(금요일, 평일)~2024.3.28
private val START_DATE: LocalDate = LocalDate.of(2024, 3, 1)
private val END_DATE: LocalDate = START_DATE.plusDays(27)

/**
 * UI 테스트에서 공통으로 사용하는 영화/예매 fixture
 */
fun harryPotterMovie(): Movie = Movie(
    id = 0,
    description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
    screening = Screening(ScreeningPeriod(START_DATE, END_DATE), RunningTime(152)),
)

fun harryPotterReservation(): Reservation = Reservation.of(harryPotterMovie())
