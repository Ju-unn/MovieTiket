package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SeatGradeTest {

    @Test
    @DisplayName("1,2행은 B등급(10,000원)이다")
    fun bGrade() {
        assertThat(SeatGrade.of("A1")).isEqualTo(SeatGrade.B)
        assertThat(SeatGrade.of("B4")).isEqualTo(SeatGrade.B)
        assertThat(SeatGrade.B.price()).isEqualTo(Money(10_000))
    }

    @Test
    @DisplayName("3,4행은 S등급(15,000원)이다")
    fun sGrade() {
        assertThat(SeatGrade.of("C1")).isEqualTo(SeatGrade.S)
        assertThat(SeatGrade.of("D4")).isEqualTo(SeatGrade.S)
        assertThat(SeatGrade.S.price()).isEqualTo(Money(15_000))
    }

    @Test
    @DisplayName("5행은 A등급(12,000원)이다")
    fun aGrade() {
        assertThat(SeatGrade.of("E1")).isEqualTo(SeatGrade.A)
        assertThat(SeatGrade.A.price()).isEqualTo(Money(12_000))
    }
}
