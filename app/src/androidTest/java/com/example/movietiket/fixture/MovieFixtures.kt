package com.example.movietiket.fixture

import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieDescription
import com.example.movietiket.model.MovieTitle
import com.example.movietiket.model.Reservation
import com.example.movietiket.model.RunningTime
import com.example.movietiket.model.Screening
import com.example.movietiket.model.ScreeningPeriod
import com.example.movietiket.model.Synopsis
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
