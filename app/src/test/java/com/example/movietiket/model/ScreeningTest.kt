package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ScreeningTest {

    private fun screening(): Screening {
        return Screening(ScreeningDate("2024.3.1"), RunningTime(152))
    }

    @Test
    @DisplayName("상영일을 표시용 값으로 조회한다")
    fun displayDate() {
        assertThat(screening().displayDate()).isEqualTo("2024.3.1")
    }

    @Test
    @DisplayName("러닝타임을 분 단위로 조회한다")
    fun runningTimeMinutes() {
        assertThat(screening().runningTimeMinutes()).isEqualTo(152)
    }
}
