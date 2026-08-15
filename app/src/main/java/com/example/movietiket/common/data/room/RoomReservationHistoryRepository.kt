package com.example.movietiket.common.data.room

import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.ReservationHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room 기반 예매 내역 저장소 구현
 */
class RoomReservationHistoryRepository(
    private val dao: ReservationDao,
    private val now: () -> Long = System::currentTimeMillis,
) : ReservationHistoryRepository {

    // 예매 내역을 Entity로 변환해 저장한다
    override suspend fun save(reservation: Reservation): Long =
        dao.insert(reservation.toEntity(reservedAt = now()))

    // 전체 예매 내역을 도메인 객체 목록으로 변환해 반환한다
    override fun findAll(): Flow<List<ReservationHistory>> =
        dao.findAll().map { entities -> entities.map { it.toReservationHistory() } }

    // id로 예매 내역 하나를 도메인 객체로 변환해 반환한다
    override suspend fun findById(id: Long): ReservationHistory? =
        dao.findById(id)?.toReservationHistory()
}
