package com.example.movietiket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.example.movietiket.common.data.SharedPreferencesPushNotificationSettings
import com.example.movietiket.notification.AlarmManagerScreeningAlarmScheduler
import com.example.movietiket.notification.PushNotificationGatedAlarmScheduler
import com.example.movietiket.notification.ScreeningAlarmScheduler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.movietiket.common.data.MovieTicketDatabase
import com.example.movietiket.common.data.RoomReservationHistoryRepository
import com.example.movietiket.common.repository.ReservationHistoryRepository
import com.example.movietiket.navigation.MovieNavigationController
import com.example.movietiket.navigation.Screen
import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.common.model.Theater
import com.example.movietiket.movielist.presenter.MovieListContract
import com.example.movietiket.movielist.presenter.MovieListPresenter
import com.example.movietiket.reservation.presenter.ReservationContract
import com.example.movietiket.reservation.presenter.ReservationPresenter
import com.example.movietiket.seat.presenter.SeatSelectionContract
import com.example.movietiket.seat.presenter.SeatSelectionPresenter
import com.example.movietiket.common.state.ReservationSaver
import com.example.movietiket.common.state.ScreenSaver
import com.example.movietiket.complete.view.ReservationCompleteScreen
import com.example.movietiket.movielist.view.MovieListScreen
import com.example.movietiket.reservation.view.MovieReservationScreen
import com.example.movietiket.seat.view.SeatSelectionScreen

/**
 * 앱의 루트 Composable
 * 현재 화면 상태(Screen)에 따라 각 화면(View)과 Presenter를 연결한다
 * 화면 상태는 rememberSaveable로 저장해 회전 등 구성 변경에도 유지한다
 */
@Composable
fun MovieTicketApp() {
    val screenState = rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.MovieList) }
    val navigationController = remember { MovieNavigationController(screenState) }
    val reservationHistoryRepository = rememberReservationHistoryRepository()
    val screeningAlarmScheduler = rememberScreeningAlarmScheduler()

    RequestNotificationPermissionOnce()

    when (val screen = navigationController.screen()) {
        is Screen.MovieList -> MovieListRoute(navigationController)
        is Screen.MovieReservation -> MovieReservationRoute(screen.movie, screen.theater, navigationController)
        is Screen.SeatSelection -> SeatSelectionRoute(
            initialReservation = screen.reservation,
            reservationHistoryRepository = reservationHistoryRepository,
            screeningAlarmScheduler = screeningAlarmScheduler,
            navigationController = navigationController,
        )
        is Screen.ReservationComplete -> ReservationCompleteRoute(screen.reservation, navigationController)
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

private class MovieListViewState : MovieListContract.View {
    var movies by mutableStateOf(emptyList<Movie>())
        private set

    // null이면 극장 선택 바텀시트를 띄우지 않는다
    var theaterSelection by mutableStateOf<List<Theater>?>(null)
        private set

    override fun showMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override fun showTheaterSelection(theaters: List<Theater>) {
        theaterSelection = theaters
    }

    override fun hideTheaterSelection() {
        theaterSelection = null
    }
}

@Composable
private fun MovieListRoute(navigationController: MovieNavigationController) {
    val view = remember { MovieListViewState() }
    val presenter = remember {
        MovieListPresenter(
            view = view,
            onMovieReserveRequested = navigationController::moveToReservation,
        )
    }

    MovieListScreen(
        movies = view.movies,
        theaterSelection = view.theaterSelection,
        onReserveClick = presenter::onReserveClick,
        onTheaterClick = presenter::onTheaterSelected,
        onTheaterSelectionDismiss = presenter::onTheaterSelectionDismissed,
    )
}

@Composable
private fun MovieReservationRoute(
    movie: Movie,
    theater: Theater,
    navigationController: MovieNavigationController,
) {
    var reservation by rememberSaveable(stateSaver = ReservationSaver) { mutableStateOf(Reservation.of(movie, theater)) }
    val view = remember {
        object : ReservationContract.View {
            override fun showReservation(newReservation: Reservation) {
                reservation = newReservation
            }
        }
    }
    // 시스템 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    val presenter = remember(view) {
        ReservationPresenter(
            initialReservation = reservation,
            view = view,
            onReservationConfirmed = navigationController::moveToSeatSelection,
        )
    }

    MovieReservationScreen(
        reservation = reservation,
        onIncreaseHeadCount = presenter::increaseHeadCount,
        onDecreaseHeadCount = presenter::decreaseHeadCount,
        onSelectDate = presenter::selectDate,
        onSelectTime = presenter::selectTime,
        onConfirmClick = presenter::confirmReservation,
        onBackClick = navigationController::moveToMovieList,
    )
}

@Composable
private fun SeatSelectionRoute(
    initialReservation: Reservation,
    reservationHistoryRepository: ReservationHistoryRepository,
    screeningAlarmScheduler: ScreeningAlarmScheduler,
    navigationController: MovieNavigationController,
) {
    val coroutineScope = rememberCoroutineScope()
    var reservation by rememberSaveable(stateSaver = ReservationSaver) { mutableStateOf(initialReservation) }
    var seatLimitExceededEventCount by remember { mutableStateOf(0) }
    val view = remember {
        object : SeatSelectionContract.View {
            override fun showReservation(newReservation: Reservation) {
                reservation = newReservation
            }

            override fun showSeatLimitExceeded() {
                seatLimitExceededEventCount++
            }
        }
    }

    // 좌석 선택 화면에서 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    val presenter = remember(view) {
        SeatSelectionPresenter(
            initialReservation = reservation,
            view = view,
            reservationHistoryRepository = reservationHistoryRepository,
            screeningAlarmScheduler = screeningAlarmScheduler,
            coroutineScope = coroutineScope,
            onSelectionConfirmed = navigationController::moveToReservationComplete,
        )
    }

    SeatSelectionScreen(
        reservation = reservation,
        seatLimitExceededEvent = seatLimitExceededEventCount,
        onSelectSeat = presenter::selectSeat,
        onDeselectSeat = presenter::deselectSeat,
        onConfirmClick = presenter::confirmSelection,
        onBackClick = navigationController::moveToMovieList,
    )
}

@Composable
private fun ReservationCompleteRoute(
    reservation: Reservation,
    navigationController: MovieNavigationController,
) {
    // 완료 화면에서 뒤로 가기 시 영화 목록으로 돌아간다
    BackHandler { navigationController.moveToMovieList() }

    ReservationCompleteScreen(
        reservation = reservation,
        onBackClick = navigationController::moveToMovieList,
    )
}
