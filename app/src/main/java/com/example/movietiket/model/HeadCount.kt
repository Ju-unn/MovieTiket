package com.example.movietiket.model

/**
 * 예매 인원을 포장한 값 객체 (최소 1명)
 */
@JvmInline
value class HeadCount(private val value: Int) {
    init {
        require(value >= MINIMUM_COUNT) { "예매 인원은 최소 ${MINIMUM_COUNT}명 이상이어야 한다" }
    }

    fun increase(): HeadCount = HeadCount(value + 1)

    fun decrease(): HeadCount {
        // 최소 인원 밑으로는 줄일 수 없다
        if (value == MINIMUM_COUNT) {
            return this
        }
        return HeadCount(value - 1)
    }

    fun totalPriceWith(ticketPrice: Money): Money = ticketPrice * value

    fun toDisplayValue(): String = value.toString()

    fun toInt(): Int = value

    companion object {
        private const val MINIMUM_COUNT = 1
        val MINIMUM = HeadCount(MINIMUM_COUNT)
    }
}
