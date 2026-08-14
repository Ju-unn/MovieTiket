package com.example.movietiket.common.model.reservation

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Money 값 객체의 생성 규칙과 연산을 검증하는 테스트
 */
class MoneyTest {

    // 음수 금액은 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("금액은 음수로 생성할 수 없다")
    fun cannotCreateNegativeAmount() {
        assertThatIllegalArgumentException()
            .isThrownBy { Money(-1) }
    }

    // 금액에 수량을 곱한 값이 정확히 계산되는지 검증
    @Test
    @DisplayName("금액에 수량을 곱할 수 있다")
    fun multiply() {
        val money = Money(13_000)

        val multiplied = money * 3

        assertThat(multiplied).isEqualTo(Money(39_000))
    }

    // 금액이 원 단위 숫자로 정상 조회되는지 검증
    @Test
    @DisplayName("금액을 원 단위 숫자로 조회할 수 있다")
    fun displayValue() {
        val money = Money(26_000)

        assertThat(money.toWon()).isEqualTo(26_000)
    }
}
