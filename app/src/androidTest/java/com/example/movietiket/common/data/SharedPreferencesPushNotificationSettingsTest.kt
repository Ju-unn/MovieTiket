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

    @After
    fun tearDown() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().clear().apply()
    }

    @Test
    fun defaultsToEnabled() {
        val settings = SharedPreferencesPushNotificationSettings(context)

        assertTrue(settings.isEnabled())
    }

    @Test
    fun setEnabledPersistsAcrossInstances() {
        SharedPreferencesPushNotificationSettings(context).setEnabled(false)

        assertFalse(SharedPreferencesPushNotificationSettings(context).isEnabled())
    }
}
