package com.example.movietiket.common.model.movie

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * RunningTime 값 객체의 생성 규칙을 검증하는 테스트
 */
class RunningTimeTest {

    // 1분 이상의 유효한 러닝타임이 정상 생성되는지 검증
    @Test
    @DisplayName("1분 이상의 러닝타임을 생성할 수 있다")
    fun createValidRunningTime() {
        val runningTime = RunningTime(152)

        assertThat(runningTime.toMinutes()).isEqualTo(152)
    }

    // 0분 이하의 러닝타임은 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("0분 이하의 러닝타임은 생성할 수 없다")
    fun cannotCreateNonPositiveRunningTime() {
        assertThatIllegalArgumentException()
            .isThrownBy { RunningTime(0) }
    }
}
