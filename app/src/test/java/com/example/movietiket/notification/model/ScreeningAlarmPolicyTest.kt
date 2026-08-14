package com.example.movietiket.notification.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * 상영 시각 대비 현재 시각에 따른 알림 예약 시점(예약/즉시/생략) 정책을 검증한다.
 */
class ScreeningAlarmPolicyTest {

    private val screeningAt: LocalDateTime = LocalDateTime.of(2026, 8, 9, 20, 0)

    // 상영 30분 전보다 이르면 상영 30분 전 시각으로 예약되는지 검증
    @Test
    @DisplayName("상영 30분 전보다 이르면 상영 30분 전으로 알림을 예약한다")
    fun schedulesAtNoticeTime() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt.minusHours(3))

        assertThat(schedule).isEqualTo(AlarmSchedule.At(LocalDateTime.of(2026, 8, 9, 19, 30)))
    }

    // 상영 30분 전 직전 시점에도 여전히 예약 대상인지 검증
    @Test
    @DisplayName("상영 30분 전 직전이면 아직 예약 대상이다")
    fun justBeforeNoticeTime() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt.minusMinutes(31))

        assertThat(schedule).isInstanceOf(AlarmSchedule.At::class.java)
    }

    // 정확히 상영 30분 전이면 즉시 알림 처리되는지 검증
    @Test
    @DisplayName("정확히 상영 30분 전이면 즉시 알린다")
    fun exactlyAtNoticeTime() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt.minusMinutes(30))

        assertThat(schedule).isEqualTo(AlarmSchedule.Immediately)
    }

    // 상영 30분 이내로 들어오면 즉시 알림 처리되는지 검증
    @Test
    @DisplayName("상영 30분 이내로 들어왔으면 즉시 알린다")
    fun withinNoticeWindow() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt.minusMinutes(5))

        assertThat(schedule).isEqualTo(AlarmSchedule.Immediately)
    }

    // 상영이 시작된 뒤에는 알림이 생략되는지 검증
    @Test
    @DisplayName("상영이 시작된 뒤에는 알리지 않는다")
    fun afterScreeningStarted() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt)

        assertThat(schedule).isEqualTo(AlarmSchedule.Skip)
    }

    // 이미 지난 상영은 알림이 생략되는지 검증
    @Test
    @DisplayName("이미 지난 상영은 알리지 않는다")
    fun pastScreening() {
        val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now = screeningAt.plusDays(1))

        assertThat(schedule).isEqualTo(AlarmSchedule.Skip)
    }
}
