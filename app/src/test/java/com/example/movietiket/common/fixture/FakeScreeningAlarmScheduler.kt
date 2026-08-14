package com.example.movietiket.common.fixture

import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.notification.ScreeningAlarmScheduler

/**
 * AlarmManager 없이 Presenter를 검증하기 위한 가짜 스케줄러
 */
class FakeScreeningAlarmScheduler : ScreeningAlarmScheduler {

    val scheduled = mutableListOf<Pair<Long, Reservation>>()

    override fun schedule(reservationId: Long, reservation: Reservation) {
        scheduled += reservationId to reservation
    }
}
