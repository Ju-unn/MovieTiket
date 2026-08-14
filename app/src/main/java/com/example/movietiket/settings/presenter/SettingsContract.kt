package com.example.movietiket.settings.presenter

/**
 * 설정 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface SettingsContract {

    /** 설정 화면에 상태를 표시하는 View */
    interface View {
        // 푸시 알림 활성화 상태를 표시한다
        fun showPushNotificationEnabled(enabled: Boolean)
    }

    /** 설정 화면의 사용자 입력을 처리하는 Presenter */
    interface Presenter {
        // 저장된 설정을 불러온다
        fun loadSettings()
        // 푸시 알림 토글 값을 반영한다
        fun onPushNotificationToggled(enabled: Boolean)
    }
}
