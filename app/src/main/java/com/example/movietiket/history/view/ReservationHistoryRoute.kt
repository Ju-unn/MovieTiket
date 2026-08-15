package com.example.movietiket.history.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.ReservationHistoryRepository
import com.example.movietiket.history.presenter.ReservationHistoryContract
import com.example.movietiket.history.presenter.ReservationHistoryPresenter
import com.example.movietiket.navigation.MovieNavigationController

/** 예매 내역 화면의 상태를 보관하는 View 구현체 */
private class ReservationHistoryViewState : ReservationHistoryContract.View {
    var histories by mutableStateOf(emptyList<ReservationHistory>())
        private set

    override fun showHistories(histories: List<ReservationHistory>) {
        this.histories = histories
    }
}

// 예매 내역 화면을 보여준다
@Composable
internal fun ReservationHistoryRoute(
    reservationHistoryRepository: ReservationHistoryRepository,
    navigationController: MovieNavigationController,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val view = remember { ReservationHistoryViewState() }
    val presenter = remember {
        ReservationHistoryPresenter(
            view = view,
            reservationHistoryRepository = reservationHistoryRepository,
            coroutineScope = coroutineScope,
            onHistorySelected = { navigationController.moveToReservationDetail(it.reservation()) },
        )
    }

    ReservationHistoryScreen(
        histories = view.histories,
        onHistoryClick = presenter::onHistoryClick,
        modifier = modifier,
    )
}
