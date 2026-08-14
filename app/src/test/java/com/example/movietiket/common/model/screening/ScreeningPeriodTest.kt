package com.example.movietiket.common.model.screening

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ScreeningPeriodTest {

    @Test
    @DisplayName("종료일이 시작일보다 빠르면 생성할 수 없다")
    fun cannotCreateWhenEndBeforeStart() {
        assertThatIllegalArgumentException()
            .isThrownBy { ScreeningPeriod(LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 1)) }
    }

    @Test
    @DisplayName("시작일부터 종료일까지의 모든 날짜를 조회한다")
    fun dates() {
        val period = ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3))

        assertThat(period.dates()).containsExactly(
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 3, 2),
            LocalDate.of(2024, 3, 3),
        )
    }

    @Test
    @DisplayName("기간 내 날짜인지 확인할 수 있다")
    fun contains() {
        val period = ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28))

        assertThat(period.contains(LocalDate.of(2024, 3, 1))).isTrue()
        assertThat(period.contains(LocalDate.of(2024, 3, 28))).isTrue()
        assertThat(period.contains(LocalDate.of(2024, 2, 29))).isFalse()
        assertThat(period.contains(LocalDate.of(2024, 3, 29))).isFalse()
    }

    @Test
    @DisplayName("상영 기간을 표시용 값으로 조회한다")
    fun displayValue() {
        val period = ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28))

        assertThat(period.displayValue()).isEqualTo("2024.3.1 ~ 2024.3.28")
    }
}
