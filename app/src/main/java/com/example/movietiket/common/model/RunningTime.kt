package com.example.movietiket.common.model

/**
 * 러닝타임(분)을 포장한 값 객체
 */
@JvmInline
value class RunningTime(private val minutes: Int) {
    init {
        require(minutes > 0) { "러닝타임은 1분 이상이어야 한다" }
    }

    fun toMinutes(): Int = minutes
}
