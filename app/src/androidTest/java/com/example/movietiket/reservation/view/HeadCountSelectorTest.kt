package com.example.movietiket.reservation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 인원 수 선택 컴포넌트(HeadCountSelector)의 표시와 증감 버튼 동작을 검증한다
 */
@RunWith(AndroidJUnit4::class)
class HeadCountSelectorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 현재 인원 수가 화면에 표시되는지 검증한다
    @Test
    fun `현재_인원_수를_표시한다`() {
        composeTestRule.setContent {
            MovieTiketTheme {
                HeadCountSelector(headCount = "1", onIncrease = {}, onDecrease = {})
            }
        }

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    // 플러스 버튼 클릭 시 증가 콜백이 호출되는지 검증한다
    @Test
    fun `플러스_버튼을_클릭하면_증가_콜백이_호출된다`() {
        var increased = false

        composeTestRule.setContent {
            MovieTiketTheme {
                HeadCountSelector(headCount = "1", onIncrease = { increased = true }, onDecrease = {})
            }
        }

        composeTestRule.onNodeWithText("+").performClick()

        assertTrue(increased)
    }

    // 마이너스 버튼 클릭 시 감소 콜백이 호출되는지 검증한다
    @Test
    fun `마이너스_버튼을_클릭하면_감소_콜백이_호출된다`() {
        var decreased = false

        composeTestRule.setContent {
            MovieTiketTheme {
                HeadCountSelector(headCount = "1", onIncrease = {}, onDecrease = { decreased = true })
            }
        }

        composeTestRule.onNodeWithText("-").performClick()

        assertTrue(decreased)
    }
}
