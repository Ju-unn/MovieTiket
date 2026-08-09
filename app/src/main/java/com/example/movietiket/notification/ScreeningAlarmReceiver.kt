package com.example.movietiket.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 예약된 시각에 깨어나 상영 알림을 띄운다
 */
class ScreeningAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reservationId = intent.getLongExtra(EXTRA_RESERVATION_ID, NO_RESERVATION_ID)
        val movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE) ?: return
        if (reservationId == NO_RESERVATION_ID) return

        ScreeningNotifier.notifyScreeningSoon(context, reservationId, movieTitle)
    }

    companion object {
        const val EXTRA_RESERVATION_ID = "reservationId"
        const val EXTRA_MOVIE_TITLE = "movieTitle"
        private const val NO_RESERVATION_ID = -1L
    }
}
