package com.example.movietiket.history.presenter

import com.example.movietiket.common.model.reservation.ReservationHistory

/**
 * 예매 내역 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface ReservationHistoryContract {

    /** 예매 내역 화면에 데이터를 표시하는 View */
    interface View {
        // 예매 내역 목록을 화면에 표시
        fun showHistories(histories: List<ReservationHistory>)
    }

    /** 예매 내역 화면의 동작을 정의하는 Presenter */
    interface Presenter {
        // 예매 내역을 불러온다
        fun loadHistories()
        // 예매 내역 항목 클릭을 처리한다
        fun onHistoryClick(history: ReservationHistory)
    }
}
