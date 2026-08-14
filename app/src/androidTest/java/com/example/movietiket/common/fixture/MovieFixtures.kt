package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.movie.MovieDescription
import com.example.movietiket.common.model.movie.MovieTitle
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.movie.RunningTime
import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import com.example.movietiket.common.model.movie.Synopsis
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.model.theater.TheaterName
import java.time.LocalDate

// 2024.3.1(금요일, 평일)~2024.3.28
private val START_DATE: LocalDate = LocalDate.of(2024, 3, 1)
private val END_DATE: LocalDate = START_DATE.plusDays(27)

// UI 테스트에서 공통으로 사용하는 영화/예매 fixture

// 테스트용 해리 포터 영화 데이터를 생성한다
fun harryPotterMovie(): Movie = Movie(
    id = 0,
    description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
    screening = Screening(ScreeningPeriod(START_DATE, END_DATE), RunningTime(152)),
)

// 테스트용 강남점 극장 데이터를 생성한다
fun gangnamTheater(): Theater = Theater(id = 0, name = TheaterName("강남점"))

// 테스트용 해리 포터 예매 데이터를 생성한다
fun harryPotterReservation(): Reservation = Reservation.of(harryPotterMovie(), gangnamTheater())
