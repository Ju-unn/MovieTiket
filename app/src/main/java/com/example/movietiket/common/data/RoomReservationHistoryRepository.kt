package com.example.movietiket.common.data

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

    override suspend fun save(reservation: Reservation): Long =
        dao.insert(reservation.toEntity(reservedAt = now()))

    override fun findAll(): Flow<List<ReservationHistory>> =
        dao.findAll().map { entities -> entities.map { it.toReservationHistory() } }

    override suspend fun findById(id: Long): ReservationHistory? =
        dao.findById(id)?.toReservationHistory()
}
