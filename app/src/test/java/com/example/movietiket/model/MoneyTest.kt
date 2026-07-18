package com.example.movietiket.model

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
    @DisplayName("천 단위 구분 기호와 원 단위로 표시한다")
    fun displayValue() {
        val money = Money(26_000)

        assertThat(money.toDisplayValue()).isEqualTo("26,000원")
    }
}
