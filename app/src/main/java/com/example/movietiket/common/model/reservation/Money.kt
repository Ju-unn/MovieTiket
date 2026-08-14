package com.example.movietiket.common.model.reservation

/**
 * 금액(원)을 포장한 값 객체
 */
@JvmInline
value class Money(private val amount: Int) {
    init {
        require(amount >= 0) { "금액은 0원 이상이어야 한다" }
    }

    // 금액에 수량을 곱한 새 금액을 반환한다
    operator fun times(count: Int): Money = Money(amount * count)

    fun toWon(): Int = amount
}
