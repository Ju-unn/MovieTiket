package com.example.movietiket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.movietiket.common.data.preferences.SharedPreferencesPushNotificationSettings
import com.example.movietiket.common.data.room.MovieTicketDatabase
import com.example.movietiket.common.data.room.RoomReservationHistoryRepository
import com.example.movietiket.common.repository.ReservationHistoryRepository
import com.example.movietiket.common.state.ScreenSaver
import com.example.movietiket.common.view.MovieBottomNavigation
import com.example.movietiket.common.view.MovieTopBar
import com.example.movietiket.complete.view.ReservationCompleteRoute
import com.example.movietiket.complete.view.ReservationDetailRoute
import com.example.movietiket.history.view.ReservationHistoryRoute
import com.example.movietiket.movielist.view.MovieListRoute
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.navigation.Screen
import com.example.movietiket.notification.AlarmManagerScreeningAlarmScheduler
import com.example.movietiket.notification.PushNotificationGatedAlarmScheduler
import com.example.movietiket.notification.ScreeningAlarmScheduler
import com.example.movietiket.reservation.view.MovieReservationRoute
import com.example.movietiket.seat.view.SeatSelectionRoute
import com.example.movietiket.settings.view.SettingsRoute

/**
 * 앱의 루트 Composable
 * 현재 화면 상태(Screen)에 따라 각 화면의 Route를 띄운다
 * 화면별 View 구현체와 Presenter 연결은 각 기능 패키지의 Route가 담당한다
 * 화면 상태는 rememberSaveable로 저장해 회전 등 구성 변경에도 유지한다
 */
@Composable
fun MovieTicketApp(
    pendingReservationId: Long? = null,
    onPendingReservationConsumed: () -> Unit = {},
) {
    val screenState = rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.Tab.Home) }
    val navigationController = remember { MovieNavigationController(screenState) }
    val reservationHistoryRepository = rememberReservationHistoryRepository()
    val screeningAlarmScheduler = rememberScreeningAlarmScheduler()

    RequestNotificationPermissionOnce()
    OpenPendingReservationFromNotification(
        pendingReservationId = pendingReservationId,
        reservationHistoryRepository = reservationHistoryRepository,
        navigationController = navigationController,
        onConsumed = onPendingReservationConsumed,
    )

    when (val screen = navigationController.screen()) {
        is Screen.Tab -> TabRoute(screen, navigationController, reservationHistoryRepository)
        is Screen.MovieReservation -> MovieReservationRoute(screen.movie, screen.theater, navigationController)
        is Screen.SeatSelection -> SeatSelectionRoute(
            initialReservation = screen.reservation,
            reservationHistoryRepository = reservationHistoryRepository,
            screeningAlarmScheduler = screeningAlarmScheduler,
            navigationController = navigationController,
        )
        is Screen.ReservationComplete -> ReservationCompleteRoute(screen.reservation, navigationController)
        is Screen.ReservationDetail -> ReservationDetailRoute(screen.reservation, navigationController)
    }
}

/**
 * 예매 내역 / 홈 / 설정 탭의 공통 골격
 * 상단바와 하단 네비게이션은 고정하고 가운데 본문만 탭에 따라 바꾼다
 */
@Composable
private fun TabRoute(
    tab: Screen.Tab,
    navigationController: MovieNavigationController,
    reservationHistoryRepository: ReservationHistoryRepository,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MovieTopBar() },
        bottomBar = {
            MovieBottomNavigation(
                currentTab = tab,
                onTabClick = navigationController::moveToTab,
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (tab) {
            Screen.Tab.History -> ReservationHistoryRoute(
                reservationHistoryRepository = reservationHistoryRepository,
                navigationController = navigationController,
                modifier = contentModifier,
            )
            Screen.Tab.Home -> MovieListRoute(navigationController, modifier = contentModifier)
            Screen.Tab.Settings -> SettingsRoute(modifier = contentModifier)
        }
    }
}

/** 앱 전역에서 하나의 Room 인스턴스를 공유한다 */
@Composable
private fun rememberReservationHistoryRepository(): ReservationHistoryRepository {
    val context = LocalContext.current
    return remember(context) {
        RoomReservationHistoryRepository(MovieTicketDatabase.getInstance(context).reservationDao())
    }
}

/** 푸시 알림이 꺼져 있으면 예약하지 않도록 감싼다 */
@Composable
private fun rememberScreeningAlarmScheduler(): ScreeningAlarmScheduler {
    val context = LocalContext.current
    return remember(context) {
        PushNotificationGatedAlarmScheduler(
            delegate = AlarmManagerScreeningAlarmScheduler(context),
            pushNotificationSettings = SharedPreferencesPushNotificationSettings(context),
        )
    }
}

/**
 * Android 13+에서는 알림을 띄우려면 런타임 권한이 필요하다
 * 앱을 처음 띄울 때 한 번만 요청한다
 */
@Composable
private fun RequestNotificationPermissionOnce() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(RequestPermission()) { }
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        if (!granted) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

/** 알림을 눌러 들어왔으면 해당 예매 정보 화면으로 이동한다 */
@Composable
private fun OpenPendingReservationFromNotification(
    pendingReservationId: Long?,
    reservationHistoryRepository: ReservationHistoryRepository,
    navigationController: MovieNavigationController,
    onConsumed: () -> Unit,
) {
    LaunchedEffect(pendingReservationId) {
        val id = pendingReservationId ?: return@LaunchedEffect
        reservationHistoryRepository.findById(id)?.let { history ->
            navigationController.moveToReservationDetail(history.reservation())
        }
        onConsumed()
    }
}
