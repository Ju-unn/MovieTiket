package com.example.movietiket.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.example.movietiket.BuildConfig
import com.example.movietiket.common.model.reservation.Reservation
import com.example.movietiket.notification.model.AlarmSchedule
import com.example.movietiket.notification.model.ScreeningAlarmPolicy
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * AlarmManager로 상영 30분 전 알림을 예약하는 구현체
 */
class AlarmManagerScreeningAlarmScheduler(
    private val context: Context,
    private val now: () -> LocalDateTime = LocalDateTime::now,
) : ScreeningAlarmScheduler {

    private val alarmManager: AlarmManager? = context.getSystemService()

    override fun schedule(reservationId: Long, reservation: Reservation) {
        val screeningAt = reservation.screeningDateTime() ?: return
        val movieTitle = reservation.displayMovieTitle()

        when (val schedule = ScreeningAlarmPolicy.scheduleFor(screeningAt, now())) {
            is AlarmSchedule.At -> setAlarm(reservationId, movieTitle, schedule.triggerAt)
            AlarmSchedule.Immediately ->
                ScreeningNotifier.notifyScreeningSoon(context, reservationId, movieTitle)
            AlarmSchedule.Skip -> scheduleDebugAlarmForPastScreening(reservationId, movieTitle)
        }
    }

    private fun setAlarm(reservationId: Long, movieTitle: String, triggerAt: LocalDateTime) {
        setAlarmAt(reservationId, movieTitle, triggerAt.toEpochMilli())
    }

    /**
     * 고정 상영 데이터가 과거(2024년)라 실기기에서는 알림이 한 번도 울리지 않는다
     * 디버그 빌드에서만 잠깐 뒤에 울려 동작을 눈으로 확인할 수 있게 한다
     */
    private fun scheduleDebugAlarmForPastScreening(reservationId: Long, movieTitle: String) {
        if (!BuildConfig.DEBUG) return
        Log.d(TAG, "상영이 이미 지나 ${DEBUG_DELAY_SECONDS}초 뒤 확인용 알림을 예약한다: $movieTitle")
        setAlarmAt(
            reservationId,
            movieTitle,
            System.currentTimeMillis() + DEBUG_DELAY_SECONDS * MILLIS_PER_SECOND,
        )
    }

    private fun setAlarmAt(reservationId: Long, movieTitle: String, triggerAtMillis: Long) {
        val alarmManager = alarmManager ?: return
        val pendingIntent = alarmIntent(reservationId, movieTitle)

        // 정확 알람 권한이 없으면 시스템이 시각을 조정하는 방식으로라도 예약한다
        if (canScheduleExactAlarms(alarmManager)) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    private fun canScheduleExactAlarms(alarmManager: AlarmManager): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    private fun alarmIntent(reservationId: Long, movieTitle: String): PendingIntent {
        val intent = Intent(context, ScreeningAlarmReceiver::class.java).apply {
            putExtra(ScreeningAlarmReceiver.EXTRA_RESERVATION_ID, reservationId)
            putExtra(ScreeningAlarmReceiver.EXTRA_MOVIE_TITLE, movieTitle)
        }
        return PendingIntent.getBroadcast(
            context,
            reservationId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun LocalDateTime.toEpochMilli(): Long =
        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    companion object {
        private const val TAG = "ScreeningAlarm"
        private const val DEBUG_DELAY_SECONDS = 10L
        private const val MILLIS_PER_SECOND = 1_000L
    }
}
