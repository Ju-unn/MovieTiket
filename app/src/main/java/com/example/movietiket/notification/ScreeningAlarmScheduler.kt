package com.example.movietiket.notification

import com.example.movietiket.common.model.reservation.Reservation

/**
 * 예매가 확정되면 상영 30분 전 알림을 예약한다
 * Presenter가 AlarmManager를 직접 알지 않도록 인터페이스로 분리한다
 */
interface ScreeningAlarmScheduler {

    fun schedule(reservationId: Long, reservation: Reservation)
}
