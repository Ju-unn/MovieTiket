package com.example.movietiket.notification

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movietiket.common.data.preferences.SharedPreferencesPushNotificationSettings
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 알림이 실제로 뜨는지는 API 33+ 알림 권한에 좌우돼 기기마다 달라진다
 * 여기서는 알림을 띄우면 안 되는 조건(정보 누락, 푸시 설정 꺼짐)에서
 * 예외 없이 안전하게 무시하는지를 확인한다
 */
@RunWith(AndroidJUnit4::class)
class ScreeningAlarmReceiverTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val receiver = ScreeningAlarmReceiver()

    // 테스트가 끝나면 설정값과 알림을 초기화한다
    @After
    fun tearDown() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().clear().apply()
        NotificationManagerCompat.from(context).cancelAll()
    }

    // 예매 id가 없는 인텐트는 무시하는지 검증한다
    @Test
    fun ignoresIntentWithoutReservationId() {
        val intent = Intent().putExtra(ScreeningAlarmReceiver.EXTRA_MOVIE_TITLE, "해리 포터")

        receiver.onReceive(context, intent)

        assertTrue(NotificationManagerCompat.from(context).activeNotifications.isEmpty())
    }

    // 영화 제목이 없는 인텐트는 무시하는지 검증한다
    @Test
    fun ignoresIntentWithoutMovieTitle() {
        val intent = Intent().putExtra(ScreeningAlarmReceiver.EXTRA_RESERVATION_ID, 1L)

        receiver.onReceive(context, intent)

        assertTrue(NotificationManagerCompat.from(context).activeNotifications.isEmpty())
    }

    // 푸시 설정이 꺼져 있으면 알림을 띄우지 않는지 검증한다
    @Test
    fun doesNotNotifyWhenPushDisabled() {
        SharedPreferencesPushNotificationSettings(context).setEnabled(false)
        val intent = Intent()
            .putExtra(ScreeningAlarmReceiver.EXTRA_RESERVATION_ID, 1L)
            .putExtra(ScreeningAlarmReceiver.EXTRA_MOVIE_TITLE, "해리 포터")

        receiver.onReceive(context, intent)

        assertTrue(NotificationManagerCompat.from(context).activeNotifications.isEmpty())
    }
}
