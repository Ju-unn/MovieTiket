package com.example.movietiket.common.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.example.movietiket.common.repository.PushNotificationSettings

/**
 * 값 하나만 저장하면 되므로 SharedPreferences로 충분하다
 */
class SharedPreferencesPushNotificationSettings(context: Context) : PushNotificationSettings {

    private val preferences =
        context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun isEnabled(): Boolean = preferences.getBoolean(KEY_PUSH_ENABLED, DEFAULT_ENABLED)

    override fun setEnabled(enabled: Boolean) {
        preferences.edit { putBoolean(KEY_PUSH_ENABLED, enabled) }
    }

    companion object {
        private const val PREFERENCES_NAME = "settings"
        private const val KEY_PUSH_ENABLED = "push_notification_enabled"

        /** 알림 권한을 받은 뒤라면 기본은 켜짐으로 둔다 */
        private const val DEFAULT_ENABLED = true
    }
}
