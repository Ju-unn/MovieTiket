package com.example.movietiket.history.presenter

import com.example.movietiket.common.model.reservation.ReservationHistory

/**
 * 예매 내역 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface ReservationHistoryContract {

    interface View {
        fun showHistories(histories: List<ReservationHistory>)
    }

    interface Presenter {
        fun loadHistories()
        fun onHistoryClick(history: ReservationHistory)
    }
}
