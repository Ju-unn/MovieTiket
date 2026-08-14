package com.example.movietiket.common.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** 예매 내역 테이블에 대한 Room DAO */
@Dao
interface ReservationDao {

    // 예매 내역을 새로 저장한다
    @Insert
    suspend fun insert(reservation: ReservationEntity): Long

    /** 최근에 예매한 것이 위로 오도록 정렬한다 */
    @Query("SELECT * FROM reservations ORDER BY reservedAt DESC")
    fun findAll(): Flow<List<ReservationEntity>>

    // id로 예매 내역 하나를 조회한다
    @Query("SELECT * FROM reservations WHERE id = :id")
    suspend fun findById(id: Long): ReservationEntity?
}
