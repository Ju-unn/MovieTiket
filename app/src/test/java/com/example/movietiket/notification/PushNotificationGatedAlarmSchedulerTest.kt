package com.example.movietiket.notification

import com.example.movietiket.common.fixture.FakePushNotificationSettings
import com.example.movietiket.common.fixture.FakeScreeningAlarmScheduler
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PushNotificationGatedAlarmSchedulerTest {

    private val delegate = FakeScreeningAlarmScheduler()

    private fun scheduler(enabled: Boolean) = PushNotificationGatedAlarmScheduler(
        delegate = delegate,
        pushNotificationSettings = FakePushNotificationSettings(enabled),
    )

    private fun reservation(): Reservation = Reservation.of(testMovie(), testTheater())

    @Test
    @DisplayName("푸시 알림이 켜져 있으면 상영 알림을 예약한다")
    fun schedulesWhenEnabled() {
        scheduler(enabled = true).schedule(1L, reservation())

        assertThat(delegate.scheduled).hasSize(1)
    }

    @Test
    @DisplayName("푸시 알림이 꺼져 있으면 상영 알림을 예약하지 않는다")
    fun doesNotScheduleWhenDisabled() {
        scheduler(enabled = false).schedule(1L, reservation())

        assertThat(delegate.scheduled).isEmpty()
    }

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
