package com.example.movietiket.history.presenter

import com.example.movietiket.common.model.reservation.ReservationHistory
import com.example.movietiket.common.repository.ReservationHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 예매 내역 화면의 흐름을 제어하는 Presenter
 * 로컬 DB의 변화를 구독해 새 예매가 생기면 목록이 자동으로 갱신된다
 */
class ReservationHistoryPresenter(
    private val view: ReservationHistoryContract.View,
    private val reservationHistoryRepository: ReservationHistoryRepository,
    private val coroutineScope: CoroutineScope,
    private val onHistorySelected: (ReservationHistory) -> Unit,
) : ReservationHistoryContract.Presenter {

    init {
        loadHistories()
    }

    // 저장소의 예매 내역 흐름을 구독해 View에 반영한다
    override fun loadHistories() {
        coroutineScope.launch {
            reservationHistoryRepository.findAll().collectLatest(view::showHistories)
        }
    }

    // 예매 내역 항목 클릭 시 선택 콜백을 호출한다
    override fun onHistoryClick(history: ReservationHistory) {
        onHistorySelected(history)
    }
}
