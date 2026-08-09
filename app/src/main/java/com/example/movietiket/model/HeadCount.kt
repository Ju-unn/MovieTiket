package com.example.movietiket.model

/**
 * 예매 인원을 포장한 값 객체 (최소 1명, 최대 20명)
 */
@JvmInline
value class HeadCount(private val value: Int) {
    init {
        require(value in MINIMUM_COUNT..MAXIMUM_COUNT) {
            "예매 인원은 ${MINIMUM_COUNT}명 이상 ${MAXIMUM_COUNT}명 이하여야 한다"
        }
    }

    fun increase(): HeadCount {
        // 좌석이 5행 x 4열(총 20석)로 고정되어 있어 최대 인원 위로는 늘릴 수 없다
        if (value == MAXIMUM_COUNT) {
            return this
        }
        return HeadCount(value + 1)
    }

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
        private const val MAXIMUM_COUNT = 20
        val MINIMUM = HeadCount(MINIMUM_COUNT)
    }
}
