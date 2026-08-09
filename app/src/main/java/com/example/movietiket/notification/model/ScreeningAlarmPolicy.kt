package com.example.movietiket.notification.model

import java.time.LocalDateTime

/**
 * 상영 30분 전 알림을 언제 띄울지 결정한다
 * (AlarmManager/Context 없이 순수하게 계산해 테스트할 수 있도록 분리했다)
 */
object ScreeningAlarmPolicy {

    const val NOTICE_MINUTES = 30L

    fun scheduleFor(screeningAt: LocalDateTime, now: LocalDateTime): AlarmSchedule {
        val noticeAt = screeningAt.minusMinutes(NOTICE_MINUTES)
        return when {
            // 아직 30분 전이 오지 않았다 -> 그 시각에 울리도록 예약한다
            now.isBefore(noticeAt) -> AlarmSchedule.At(noticeAt)
            // 이미 30분 이내로 들어왔지만 아직 상영 전이다 -> 지금 바로 알린다
            now.isBefore(screeningAt) -> AlarmSchedule.Immediately
            // 상영이 이미 시작했다 -> 알릴 이유가 없다
            else -> AlarmSchedule.Skip
        }
    }
}

sealed interface AlarmSchedule {

    /** 지정한 시각에 알림을 예약한다 */
    data class At(val triggerAt: LocalDateTime) : AlarmSchedule

    /** 이미 상영 30분 이내라 즉시 알린다 */
    data object Immediately : AlarmSchedule

    /** 상영이 지나 알리지 않는다 */
    data object Skip : AlarmSchedule
}
