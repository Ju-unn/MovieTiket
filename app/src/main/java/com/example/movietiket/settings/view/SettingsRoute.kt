package com.example.movietiket.settings.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.movietiket.common.data.preferences.SharedPreferencesPushNotificationSettings
import com.example.movietiket.settings.presenter.SettingsContract
import com.example.movietiket.settings.presenter.SettingsPresenter

/** 설정 화면의 상태를 보관하는 View 구현체 */
private class SettingsViewState : SettingsContract.View {
    var pushNotificationEnabled by mutableStateOf(true)
        private set

    override fun showPushNotificationEnabled(enabled: Boolean) {
        pushNotificationEnabled = enabled
    }
}

// 설정 화면을 보여준다
@Composable
internal fun SettingsRoute(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val view = remember { SettingsViewState() }
    val presenter = remember(context) {
        SettingsPresenter(
            view = view,
            pushNotificationSettings = SharedPreferencesPushNotificationSettings(context),
        )
    }

    SettingsScreen(
        pushNotificationEnabled = view.pushNotificationEnabled,
        onPushNotificationToggle = presenter::onPushNotificationToggled,
        modifier = modifier,
    )
}
