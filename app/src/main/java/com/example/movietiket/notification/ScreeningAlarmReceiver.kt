package com.example.movietiket.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.movietiket.common.data.preferences.SharedPreferencesPushNotificationSettings

/**
 * 예약된 시각에 깨어나 상영 알림을 띄운다
 */
class ScreeningAlarmReceiver : BroadcastReceiver() {

    // 알람 발생 시 예약 정보를 꺼내 상영 알림을 띄운다
    override fun onReceive(context: Context, intent: Intent) {
        val reservationId = intent.getLongExtra(EXTRA_RESERVATION_ID, NO_RESERVATION_ID)
        val movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE) ?: return
        if (reservationId == NO_RESERVATION_ID) return
        // 예약해 둔 뒤에 토글을 껐을 수 있으므로 띄우기 직전에 다시 확인한다
        if (!SharedPreferencesPushNotificationSettings(context).isEnabled()) return

        ScreeningNotifier.notifyScreeningSoon(context, reservationId, movieTitle)
    }

    companion object {
        const val EXTRA_RESERVATION_ID = "reservationId"
        const val EXTRA_MOVIE_TITLE = "movieTitle"
        private const val NO_RESERVATION_ID = -1L
    }
}
