package com.example.movietiket.notification

import com.example.movietiket.common.fixture.FakePushNotificationSettings
import com.example.movietiket.common.fixture.FakeScreeningAlarmScheduler
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 푸시 알림 설정 여부에 따라 상영 알림 예약을 막거나 위임하는 스케줄러를 검증한다.
 */
class PushNotificationGatedAlarmSchedulerTest {

    private val delegate = FakeScreeningAlarmScheduler()

    // 지정된 활성화 여부로 스케줄러 생성
    private fun scheduler(enabled: Boolean) = PushNotificationGatedAlarmScheduler(
        delegate = delegate,
        pushNotificationSettings = FakePushNotificationSettings(enabled),
    )

    // 테스트용 예매 생성
    private fun reservation(): Reservation = Reservation.of(testMovie(), testTheater())

    // 푸시 알림이 켜져 있으면 상영 알림이 예약되는지 검증
    @Test
    @DisplayName("푸시 알림이 켜져 있으면 상영 알림을 예약한다")
    fun schedulesWhenEnabled() {
        scheduler(enabled = true).schedule(1L, reservation())

        assertThat(delegate.scheduled).hasSize(1)
    }

    // 푸시 알림이 꺼져 있으면 상영 알림이 예약되지 않는지 검증
    @Test
    @DisplayName("푸시 알림이 꺼져 있으면 상영 알림을 예약하지 않는다")
    fun doesNotScheduleWhenDisabled() {
        scheduler(enabled = false).schedule(1L, reservation())

        assertThat(delegate.scheduled).isEmpty()
    }

    // 예약마다 설정을 재확인해 도중에 꺼지면 이후 예약이 중단되는지 검증
    @Test
    @DisplayName("예약할 때마다 설정을 확인하므로 도중에 꺼지면 그때부터 예약하지 않는다")
    fun checksSettingOnEveryCall() {
        val settings = FakePushNotificationSettings(enabled = true)
        val scheduler = PushNotificationGatedAlarmScheduler(delegate, settings)

        scheduler.schedule(1L, reservation())
        settings.setEnabled(false)
        scheduler.schedule(2L, reservation())

        assertThat(delegate.scheduled.map { it.first }).containsExactly(1L)
    }
}
