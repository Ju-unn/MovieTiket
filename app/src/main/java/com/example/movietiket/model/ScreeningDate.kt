package com.example.movietiket.model

/**
 * 상영일을 포장한 값 객체 (형식: yyyy.M.d)
 */
@JvmInline
value class ScreeningDate(private val value: String) {
    init {
        require(DATE_FORMAT.matches(value)) { "상영일은 yyyy.M.d 형식이어야 한다: $value" }
    }

    fun toDisplayValue(): String = value

    companion object {
        private val DATE_FORMAT = Regex("""\d{4}\.\d{1,2}\.\d{1,2}""")
    }
}
