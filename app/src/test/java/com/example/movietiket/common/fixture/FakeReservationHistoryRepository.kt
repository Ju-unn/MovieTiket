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

    // 예매를 메모리에 저장하고 새 id 발급
    override suspend fun save(reservation: Reservation): Long {
        val id = nextId++
        histories.value = histories.value + ReservationHistory(id, reservation)
        return id
    }

    // 저장된 전체 예매 이력 Flow 반환
    override fun findAll(): Flow<List<ReservationHistory>> = histories

    // id로 예매 이력 단건 조회
    override suspend fun findById(id: Long): ReservationHistory? =
        histories.value.firstOrNull { it.id() == id }
}
