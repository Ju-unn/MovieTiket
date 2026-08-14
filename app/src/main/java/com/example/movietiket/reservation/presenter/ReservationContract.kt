package com.example.movietiket.reservation.presenter

import com.example.movietiket.common.model.reservation.Reservation
import java.time.LocalDate
import java.time.LocalTime

/**
 * 영화 예매 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface ReservationContract {

    /** 예매 화면에 상태를 표시하는 View */
    interface View {
        // 예매 정보를 화면에 표시한다
        fun showReservation(reservation: Reservation)
    }

    /** 예매 화면의 사용자 입력을 처리하는 Presenter */
    interface Presenter {
        // 인원 수를 늘린다
        fun increaseHeadCount()
        // 인원 수를 줄인다
        fun decreaseHeadCount()
        // 상영 날짜를 선택한다
        fun selectDate(date: LocalDate)
        // 상영 시간을 선택한다
        fun selectTime(time: LocalTime)
        // 예매를 확정한다
        fun confirmReservation()
    }
}
