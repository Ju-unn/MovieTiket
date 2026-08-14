package com.example.movietiket

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.example.movietiket.notification.ScreeningNotifier
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.MovieTicketApp

/**
 * 앱 진입점 — 루트 Composable(MovieTicketApp)만 띄운다
 */
class MainActivity : ComponentActivity() {

    // 상영 알림을 눌러 들어왔을 때 열어야 할 예매 id (없으면 null)
    private val pendingReservationId = mutableStateOf<Long?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ScreeningNotifier.createChannel(this)
        pendingReservationId.value = reservationIdOf(intent)
        setContent {
            MovieTiketTheme {
                MovieTicketApp(
                    pendingReservationId = pendingReservationId.value,
                    onPendingReservationConsumed = { pendingReservationId.value = null },
                )
            }
        }
    }

    // launchMode="singleTop"이라 앱이 떠 있을 때 알림을 눌러도 onCreate 대신 여기로 들어온다
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingReservationId.value = reservationIdOf(intent)
    }

    private fun reservationIdOf(intent: Intent): Long? {
        val id = intent.getLongExtra(ScreeningNotifier.EXTRA_RESERVATION_ID, -1L)
        return id.takeIf { it != -1L }
    }
}