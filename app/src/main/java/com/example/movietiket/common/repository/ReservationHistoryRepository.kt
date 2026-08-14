package com.example.movietiket.common.repository

import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import kotlinx.coroutines.flow.Flow

/**
 * 예매 내역 저장소
 * Presenter가 Room을 직접 알지 않도록 인터페이스로 분리한다 (테스트에서는 가짜 구현을 쓴다)
 */
interface ReservationHistoryRepository {

    suspend fun save(reservation: Reservation): Long

    fun findAll(): Flow<List<ReservationHistory>>

    suspend fun findById(id: Long): ReservationHistory?
}
