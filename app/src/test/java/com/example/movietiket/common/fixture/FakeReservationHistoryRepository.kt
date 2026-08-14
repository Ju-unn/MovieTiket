package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.ReservationHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Room 없이 Presenter를 검증하기 위한 메모리 저장소
 */
class FakeReservationHistoryRepository : ReservationHistoryRepository {

    private val histories = MutableStateFlow(emptyList<ReservationHistory>())
    private var nextId = 1L

    val saved: List<Reservation> get() = histories.value.map { it.reservation() }

    override suspend fun save(reservation: Reservation): Long {
        val id = nextId++
        histories.value = histories.value + ReservationHistory(id, reservation)
        return id
    }

    override fun findAll(): Flow<List<ReservationHistory>> = histories

    override suspend fun findById(id: Long): ReservationHistory? =
        histories.value.firstOrNull { it.id() == id }
}
