package com.example.movietiket.model

/**
 * 금액(원)을 포장한 값 객체
 */
@JvmInline
value class Money(private val amount: Int) {
    init {
        require(amount >= 0) { "금액은 0원 이상이어야 한다" }
    }

    operator fun times(count: Int): Money = Money(amount * count)

    // 천 단위 구분 기호를 붙여 "26,000원" 형태로 표시한다
    fun toDisplayValue(): String = "%,d원".format(amount)
}
