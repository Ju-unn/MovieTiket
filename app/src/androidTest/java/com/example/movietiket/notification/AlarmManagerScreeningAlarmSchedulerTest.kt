package com.example.movietiket.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movietiket.common.fixture.harryPotterReservation
import com.example.movietiket.common.fixture.testMovie
import com.example.movietiket.common.fixture.testTheater
import com.example.movietiket.common.model.reservation.HeadCount
import com.example.movietiket.common.model.reservation.Reservation
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 실제 AlarmManager 위에서 예약이 등록되는지 PendingIntent 존재 여부로 확인한다
 */
@RunWith(AndroidJUnit4::class)
class AlarmManagerScreeningAlarmSchedulerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // FLAG_NO_CREATE는 이미 등록된 PendingIntent가 있을 때만 non-null을 반환한다
    private fun pendingIntentExists(reservationId: Long): Boolean {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reservationId.toInt(),
            Intent(context, ScreeningAlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        )
        if (pendingIntent != null) {
            context.getSystemService(AlarmManager::class.java).cancel(pendingIntent)
            pendingIntent.cancel()
        }
        return pendingIntent != null
    }

    // 상영 시각이 미래이면 알람이 등록되는지 검증한다
    @Test
    fun schedulesAlarmWhenScreeningIsInFuture() {
        val reservation = harryPotterReservation()
        val screeningAt = requireNotNull(reservation.screeningDateTime())
        val scheduler = AlarmManagerScreeningAlarmScheduler(context, now = { screeningAt.minusHours(3) })

        scheduler.schedule(reservationId = 100L, reservation)

        assertTrue(pendingIntentExists(100L))
    }

    // 상영 일시가 선택되지 않았으면 알람을 등록하지 않는지 검증한다
    @Test
    fun doesNothingWhenScreeningDateTimeIsNotSelected() {
        val reservation = Reservation(testMovie(), HeadCount.MINIMUM, testTheater())
        val scheduler = AlarmManagerScreeningAlarmScheduler(context)

        scheduler.schedule(reservationId = 101L, reservation)

        assertFalse(pendingIntentExists(101L))
    }
}
