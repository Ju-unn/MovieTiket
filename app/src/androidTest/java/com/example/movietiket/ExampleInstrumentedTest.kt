package com.example.movietiket

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * 안드로이드 기기에서 실행되는 기본 계측 테스트 샘플.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    // 앱 컨텍스트의 패키지명이 올바른지 검증한다
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.movietiket", appContext.packageName)
    }
}