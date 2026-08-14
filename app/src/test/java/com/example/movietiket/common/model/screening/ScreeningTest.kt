package com.example.movietiket.common.model.screening

import com.example.movietiket.common.model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ScreeningTest {

    private fun screening(): Screening =
        Screening(ScreeningPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 28)), RunningTime(152))

    @Test
    @DisplayName("상영 기간을 표시용 값으로 조회한다")
    fun displayPeriod() {
        assertThat(screening().displayPeriod()).isEqualTo("2024.3.1 ~ 2024.3.28")
    }

    @Test
    @DisplayName("기본 날짜는 상영 기간의 시작일이다")
    fun defaultDate() {
        assertThat(screening().defaultDate()).isEqualTo(LocalDate.of(2024, 3, 1))
    }

    @Test
    @DisplayName("상영 기간 내의 날짜인지 확인할 수 있다")
    fun isDateAvailable() {
        assertThat(screening().isDateAvailable(LocalDate.of(2024, 3, 15))).isTrue()
        assertThat(screening().isDateAvailable(LocalDate.of(2024, 4, 1))).isFalse()
    }

    @Test
    @DisplayName("상영 기간이 아닌 날짜의 시간을 조회하면 예외가 발생한다")
    fun cannotGetTimesForUnavailableDate() {
        assertThatIllegalArgumentException()
            .isThrownBy { screening().availableTimesFor(LocalDate.of(2024, 4, 1)) }
    }

    @Test
    @DisplayName("러닝타임을 분 단위로 조회한다")
    fun runningTimeMinutes() {
        assertThat(screening().runningTimeMinutes()).isEqualTo(152)
    }
}
