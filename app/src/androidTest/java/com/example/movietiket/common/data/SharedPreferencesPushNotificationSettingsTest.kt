package com.example.movietiket.common.data

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 실제 SharedPreferences 위에서 값이 저장·조회되는지 확인한다
 */
@RunWith(AndroidJUnit4::class)
class SharedPreferencesPushNotificationSettingsTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // 테스트가 끝나면 저장된 설정값을 초기화한다
    @After
    fun tearDown() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().clear().apply()
    }

    // 기본값이 알림 활성화 상태인지 검증한다
    @Test
    fun defaultsToEnabled() {
        val settings = SharedPreferencesPushNotificationSettings(context)

        assertTrue(settings.isEnabled())
    }

    // 설정한 값이 다른 인스턴스에서도 유지되는지 검증한다
    @Test
    fun setEnabledPersistsAcrossInstances() {
        SharedPreferencesPushNotificationSettings(context).setEnabled(false)

        assertFalse(SharedPreferencesPushNotificationSettings(context).isEnabled())
    }
}
