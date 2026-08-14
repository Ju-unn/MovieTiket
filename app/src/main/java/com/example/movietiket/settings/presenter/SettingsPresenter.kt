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

    // 저장된 푸시 알림 설정을 불러와 View에 표시한다
    override fun loadSettings() {
        view.showPushNotificationEnabled(pushNotificationSettings.isEnabled())
    }

    // 푸시 알림 설정을 변경하고 결과를 View에 반영한다
    override fun onPushNotificationToggled(enabled: Boolean) {
        pushNotificationSettings.setEnabled(enabled)
        view.showPushNotificationEnabled(pushNotificationSettings.isEnabled())
    }
}
