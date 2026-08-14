package com.example.movietiket.common.fixture

import com.example.movietiket.common.repository.PushNotificationSettings

/**
 * SharedPreferences 없이 토글 동작을 검증하기 위한 메모리 설정
 */
class FakePushNotificationSettings(private var enabled: Boolean = true) : PushNotificationSettings {

    // 현재 알림 활성화 상태 반환
    override fun isEnabled(): Boolean = enabled

    // 알림 활성화 상태 변경
    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}
