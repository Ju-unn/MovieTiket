package com.example.movietiket.settings.presenter

/**
 * 설정 화면의 View/Presenter 역할을 명시하는 Contract
 */
interface SettingsContract {

    interface View {
        fun showPushNotificationEnabled(enabled: Boolean)
    }

    interface Presenter {
        fun loadSettings()
        fun onPushNotificationToggled(enabled: Boolean)
    }
}
