package com.example.movietiket.common.model.reservation

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * HeadCount 값 객체의 생성 규칙과 증감/계산 동작을 검증하는 테스트
 */
class HeadCountTest {

    // 최소값(1명) 미만은 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("예매 인원은 1명 미만으로 생성할 수 없다")
    fun cannotCreateBelowMinimum() {
        assertThatIllegalArgumentException()
            .isThrownBy { HeadCount(0) }
    }

    // 최대값(20명) 초과는 생성 시 예외가 발생하는지 검증
    @Test
    @DisplayName("예매 인원은 20명 초과로 생성할 수 없다")
    fun cannotCreateAboveMaximum() {
        assertThatIllegalArgumentException()
            .isThrownBy { HeadCount(21) }
    }

    // increase 호출 시 1명 증가한 새 인스턴스를 반환하는지 검증
    @Test
    @DisplayName("인원을 늘리면 1명 증가한 새 HeadCount를 반환한다")
    fun increase() {
        val headCount = HeadCount(1)

        val increased = headCount.increase()

        assertThat(increased).isEqualTo(HeadCount(2))
    }

    // 최대 인원에서 increase 호출 시 값이 유지되는지 검증
    @Test
    @DisplayName("최대 인원(20명)에서는 더 이상 늘어나지 않는다")
    fun increaseAtMaximumKeepsMaximum() {
        val headCount = HeadCount(20)

        val increased = headCount.increase()

        assertThat(increased).isEqualTo(HeadCount(20))
    }

    // decrease 호출 시 1명 감소한 새 인스턴스를 반환하는지 검증
    @Test
    @DisplayName("인원을 줄이면 1명 감소한 새 HeadCount를 반환한다")
    fun decrease() {
        val headCount = HeadCount(3)

        val decreased = headCount.decrease()

        assertThat(decreased).isEqualTo(HeadCount(2))
    }

    // 최소 인원에서 decrease 호출 시 값이 유지되는지 검증
    @Test
    @DisplayName("최소 인원(1명)에서는 더 이상 줄어들지 않는다")
    fun decreaseAtMinimumKeepsMinimum() {
        val headCount = HeadCount(1)

        val decreased = headCount.decrease()

        assertThat(decreased).isEqualTo(HeadCount(1))
    }

    // 인원 수와 단가를 곱한 총액이 정확히 계산되는지 검증
    @Test
    @DisplayName("인원 수만큼 티켓 가격을 곱한 총액을 계산한다")
    fun totalPrice() {
        val headCount = HeadCount(2)

        val totalPrice = headCount.totalPriceWith(Money(13_000))

        assertThat(totalPrice).isEqualTo(Money(26_000))
    }

    // 인원 수가 표시용 문자열로 변환되는지 검증
    @Test
    @DisplayName("인원 수를 표시용 문자열로 변환한다")
    fun displayValue() {
        val headCount = HeadCount(3)

        assertThat(headCount.toDisplayValue()).isEqualTo("3")
    }
}
