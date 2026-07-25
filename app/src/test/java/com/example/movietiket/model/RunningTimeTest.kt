package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RunningTimeTest {

    @Test
    @DisplayName("1분 이상의 러닝타임을 생성할 수 있다")
    fun createValidRunningTime() {
        val runningTime = RunningTime(152)

        assertThat(runningTime.toMinutes()).isEqualTo(152)
    }

    @Test
    @DisplayName("0분 이하의 러닝타임은 생성할 수 없다")
    fun cannotCreateNonPositiveRunningTime() {
        assertThatIllegalArgumentException()
            .isThrownBy { RunningTime(0) }
    }
}
