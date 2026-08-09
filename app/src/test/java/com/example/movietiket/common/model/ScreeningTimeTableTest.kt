package com.example.movietiket.common.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreeningTimeTableTest {

    @Test
    @DisplayName("평일에는 오전 10시부터 2시간 간격으로 자정 전까지 상영한다")
    fun weekdayTimes() {
        val times = ScreeningTimeTable.timesFor(LocalDate.of(2024, 3, 1)) // 금요일

        assertThat(times).containsExactly(
            LocalTime.of(10, 0), LocalTime.of(12, 0), LocalTime.of(14, 0), LocalTime.of(16, 0),
            LocalTime.of(18, 0), LocalTime.of(20, 0), LocalTime.of(22, 0),
        )
    }

    @Test
    @DisplayName("주말에는 오전 9시부터 2시간 간격으로 자정 전까지 상영한다")
    fun weekendTimes() {
        val times = ScreeningTimeTable.timesFor(LocalDate.of(2024, 3, 2)) // 토요일

        assertThat(times).containsExactly(
            LocalTime.of(9, 0), LocalTime.of(11, 0), LocalTime.of(13, 0), LocalTime.of(15, 0),
            LocalTime.of(17, 0), LocalTime.of(19, 0), LocalTime.of(21, 0), LocalTime.of(23, 0),
        )
    }

    @Test
    @DisplayName("일요일도 주말 정책이 적용된다")
    fun sundayIsWeekend() {
        val times = ScreeningTimeTable.timesFor(LocalDate.of(2024, 3, 3)) // 일요일

        assertThat(times.first()).isEqualTo(LocalTime.of(9, 0))
    }
}
