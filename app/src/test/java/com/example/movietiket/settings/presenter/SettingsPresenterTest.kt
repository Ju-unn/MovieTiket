package com.example.movietiket.settings.presenter

import com.example.movietiket.common.fixture.FakePushNotificationSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SettingsPresenterTest {

    private class FakeView : SettingsContract.View {
        var pushNotificationEnabled: Boolean? = null

        override fun showPushNotificationEnabled(enabled: Boolean) {
            pushNotificationEnabled = enabled
        }
    }

    @Test
    @DisplayName("생성 시 저장된 푸시 알림 설정을 View에 통지한다")
    fun loadsSavedSetting() {
        val view = FakeView()

        SettingsPresenter(view, FakePushNotificationSettings(enabled = false))

        assertThat(view.pushNotificationEnabled).isFalse()
    }

    @Test
    @DisplayName("토글을 끄면 설정에 저장하고 View에 반영한다")
    fun turnsOff() {
        val view = FakeView()
        val settings = FakePushNotificationSettings(enabled = true)
        val presenter = SettingsPresenter(view, settings)

        presenter.onPushNotificationToggled(false)

        assertThat(settings.isEnabled()).isFalse()
        assertThat(view.pushNotificationEnabled).isFalse()
    }

    @Test
    @DisplayName("토글을 켜면 설정에 저장하고 View에 반영한다")
    fun turnsOn() {
        val view = FakeView()
        val settings = FakePushNotificationSettings(enabled = false)
        val presenter = SettingsPresenter(view, settings)

        presenter.onPushNotificationToggled(true)

        assertThat(settings.isEnabled()).isTrue()
        assertThat(view.pushNotificationEnabled).isTrue()
    }
}
