package com.example.movietiket.common.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MoneyTest {

    @Test
    @DisplayName("금액은 음수로 생성할 수 없다")
    fun cannotCreateNegativeAmount() {
        assertThatIllegalArgumentException()
            .isThrownBy { Money(-1) }
    }

    @Test
    @DisplayName("금액에 수량을 곱할 수 있다")
    fun multiply() {
        val money = Money(13_000)

        val multiplied = money * 3

        assertThat(multiplied).isEqualTo(Money(39_000))
    }

    @Test
    @DisplayName("금액을 원 단위 숫자로 조회할 수 있다")
    fun displayValue() {
        val money = Money(26_000)

        assertThat(money.toWon()).isEqualTo(26_000)
    }
}
