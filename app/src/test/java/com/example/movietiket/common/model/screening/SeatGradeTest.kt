package com.example.movietiket.common.model.screening

import com.example.movietiket.common.model.reservation.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 좌석 행 번호에 따른 좌석 등급 분류 및 등급별 가격을 검증한다.
 */
class SeatGradeTest {

    // 1,2행이 B등급(10,000원)으로 분류되는지 검증
    @Test
    @DisplayName("1,2행은 B등급(10,000원)이다")
    fun bGrade() {
        assertThat(SeatGrade.of("A1")).isEqualTo(SeatGrade.B)
        assertThat(SeatGrade.of("B4")).isEqualTo(SeatGrade.B)
        assertThat(SeatGrade.B.price()).isEqualTo(Money(10_000))
    }

    // 3,4행이 S등급(15,000원)으로 분류되는지 검증
    @Test
    @DisplayName("3,4행은 S등급(15,000원)이다")
    fun sGrade() {
        assertThat(SeatGrade.of("C1")).isEqualTo(SeatGrade.S)
        assertThat(SeatGrade.of("D4")).isEqualTo(SeatGrade.S)
        assertThat(SeatGrade.S.price()).isEqualTo(Money(15_000))
    }

    // 5행이 A등급(12,000원)으로 분류되는지 검증
    @Test
    @DisplayName("5행은 A등급(12,000원)이다")
    fun aGrade() {
        assertThat(SeatGrade.of("E1")).isEqualTo(SeatGrade.A)
        assertThat(SeatGrade.A.price()).isEqualTo(Money(12_000))
    }
}
