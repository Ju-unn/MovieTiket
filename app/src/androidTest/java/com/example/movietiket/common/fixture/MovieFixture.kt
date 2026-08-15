package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.movie.MovieDescription
import com.example.movietiket.common.model.movie.MovieTitle
import com.example.movietiket.common.model.movie.RunningTime
import com.example.movietiket.common.model.movie.Synopsis
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.screening.Screening
import com.example.movietiket.common.model.screening.ScreeningPeriod
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.model.theater.TheaterName
import java.time.LocalDate

/**
 * 테스트용 영화/극장/예매 픽스처
 *
 * app은 application 모듈이라 다른 모듈이 의존할 수 없고, 한 디렉터리를 test/androidTest
 * 두 소스셋에 등록하면 Android Studio가 참조를 풀지 못한다.
 * 그래서 이 파일은 test/ 와 androidTest/ 에 같은 내용으로 하나씩 둔다. 한쪽을 고치면 다른 쪽도 같이 고칠 것.
 */

// 2024.3.1(금요일, 평일)~2024.3.28
private val TEST_START_DATE: LocalDate = LocalDate.of(2024, 3, 1)
private val TEST_END_DATE: LocalDate = TEST_START_DATE.plusDays(27)

// 테스트용 영화(해리 포터와 마법사의 돌) 생성
fun testMovie(): Movie = Movie(
    id = 0,
    description = MovieDescription(MovieTitle("해리 포터와 마법사의 돌"), Synopsis("소개")),
    screening = Screening(ScreeningPeriod(TEST_START_DATE, TEST_END_DATE), RunningTime(152)),
)

// 테스트용 극장(강남점) 생성
fun testTheater(): Theater = Theater(id = 0, name = TheaterName("강남점"))

// 기본값(최소 인원, 상영 시작일의 첫 시간)이 채워진 테스트용 예매 생성
fun testReservation(): Reservation = Reservation.of(testMovie(), testTheater())
