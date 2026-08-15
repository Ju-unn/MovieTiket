package com.example.movietiket.common.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 예매 내역 테이블
 * 영화/극장은 고정 데이터라 id만 저장하고 조회 시 Repository에서 다시 붙인다
 */
@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val movieId: Int,
    val theaterId: Int,
    val headCount: Int,
    /** 상영 날짜 (epochDay) */
    val screeningDate: Long,
    /** 상영 시각 (secondOfDay) */
    val screeningTime: Int,
    /** 선택한 좌석을 쉼표로 이어 붙인 값 (예: "A1,B3") */
    val seats: String,
    /** 예매한 시각 (epochMilli) — 목록을 최신순으로 정렬하는 데 쓴다 */
    val reservedAt: Long,
)
