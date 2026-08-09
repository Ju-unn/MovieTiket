package com.example.movietiket.notification

import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.repository.PushNotificationSettings

/**
 * 푸시 알림이 꺼져 있으면 예약 자체를 하지 않는 데코레이터
 * (AlarmManager를 모르는 순수 클래스라 토글 동작을 유닛 테스트로 검증할 수 있다)
 */
class PushNotificationGatedAlarmScheduler(
    private val delegate: ScreeningAlarmScheduler,
    private val pushNotificationSettings: PushNotificationSettings,
) : ScreeningAlarmScheduler {

    override fun schedule(reservationId: Long, reservation: Reservation) {
        if (!pushNotificationSettings.isEnabled()) return
        delegate.schedule(reservationId, reservation)
    }
}
