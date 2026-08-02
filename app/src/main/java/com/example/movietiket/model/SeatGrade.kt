package com.example.movietiket.model

/**
 * 좌석 등급 (행 위치에 따라 가격이 달라진다: 1,2행 B / 3,4행 S / 5행 A)
 */
enum class SeatGrade(private val price: Money) {
    B(Money(10_000)),
    S(Money(15_000)),
    A(Money(12_000));

    fun price(): Money = price

    companion object {
        private const val ROWS = "ABCDE"

        fun of(seat: String): SeatGrade = when (ROWS.indexOf(seat.first()) + 1) {
            1, 2 -> B
            3, 4 -> S
            else -> A
        }
    }
}
