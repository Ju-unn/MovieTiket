package com.example.movietiket.common.repository

/**
 * 푸시 알림 수신 여부 설정
 * 설정 화면에서 켜고 끄며, 꺼져 있으면 상영 알림을 예약하지도 띄우지도 않는다
 */
interface PushNotificationSettings {

    fun isEnabled(): Boolean

    fun setEnabled(enabled: Boolean)
}
