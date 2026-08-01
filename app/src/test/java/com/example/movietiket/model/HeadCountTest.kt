package com.example.movietiket.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class HeadCountTest {

    @Test
    @DisplayName("예매 인원은 1명 미만으로 생성할 수 없다")
    fun cannotCreateBelowMinimum() {
        assertThatIllegalArgumentException()
            .isThrownBy { HeadCount(0) }
    }

    @Test
    @DisplayName("인원을 늘리면 1명 증가한 새 HeadCount를 반환한다")
    fun increase() {
        val headCount = HeadCount(1)

        val increased = headCount.increase()

        assertThat(increased).isEqualTo(HeadCount(2))
    }

    @Test
    @DisplayName("인원을 줄이면 1명 감소한 새 HeadCount를 반환한다")
    fun decrease() {
        val headCount = HeadCount(3)

        val decreased = headCount.decrease()

        assertThat(decreased).isEqualTo(HeadCount(2))
    }

    @Test
    @DisplayName("최소 인원(1명)에서는 더 이상 줄어들지 않는다")
    fun decreaseAtMinimumKeepsMinimum() {
        val headCount = HeadCount(1)

        val decreased = headCount.decrease()

        assertThat(decreased).isEqualTo(HeadCount(1))
    }

    @Test
    @DisplayName("인원 수만큼 티켓 가격을 곱한 총액을 계산한다")
    fun totalPrice() {
        val headCount = HeadCount(2)

        val totalPrice = headCount.totalPriceWith(Money(13_000))

        assertThat(totalPrice).isEqualTo(Money(26_000))
    }

    @Test
    @DisplayName("인원 수를 표시용 문자열로 변환한다")
    fun displayValue() {
        val headCount = HeadCount(3)

        assertThat(headCount.toDisplayValue()).isEqualTo("3")
    }
}
