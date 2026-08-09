package com.example.movietiket.settings.presenter

import com.example.movietiket.common.repository.PushNotificationSettings

/**
 * 설정 화면의 흐름을 제어하는 Presenter
 * 푸시 알림 토글 값을 저장소에 반영하고 결과를 View에 통지한다
 */
class SettingsPresenter(
    private val view: SettingsContract.View,
    private val pushNotificationSettings: PushNotificationSettings,
) : SettingsContract.Presenter {

    init {
        loadSettings()
    }

    override fun loadSettings() {
        view.showPushNotificationEnabled(pushNotificationSettings.isEnabled())
    }

    override fun onPushNotificationToggled(enabled: Boolean) {
        pushNotificationSettings.setEnabled(enabled)
        view.showPushNotificationEnabled(pushNotificationSettings.isEnabled())
    }
}
