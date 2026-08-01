package com.example.movietiket.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * MovieTicketApp의 화면 전환 배선(Presenter/View/NavigationController 연결)을 검증한다
 */
@RunWith(AndroidJUnit4::class)
class MovieTicketAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `목록에서_지금_예매를_클릭하면_예매_화면으로_이동한다`() {
        composeTestRule.setContent { MovieTicketApp() }

        composeTestRule.onNodeWithText("지금 예매").performClick()

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("예매 완료").assertIsDisplayed()
    }

    @Test
    fun `예매_화면에서_예매_완료를_클릭하면_완료_화면으로_이동한다`() {
        composeTestRule.setContent { MovieTicketApp() }
        composeTestRule.onNodeWithText("지금 예매").performClick()

        composeTestRule.onNodeWithText("예매 완료").performClick()

        composeTestRule.onNodeWithText("일반 1명").assertIsDisplayed()
    }

    @Test
    fun `예매_화면에서_뒤로_가기를_클릭하면_목록_화면으로_돌아간다`() {
        composeTestRule.setContent { MovieTicketApp() }
        composeTestRule.onNodeWithText("지금 예매").performClick()

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        composeTestRule.onNodeWithText("지금 예매").assertIsDisplayed()
    }

    @Test
    fun `완료_화면에서_뒤로_가기를_클릭하면_목록_화면으로_돌아간다`() {
        composeTestRule.setContent { MovieTicketApp() }
        composeTestRule.onNodeWithText("지금 예매").performClick()
        composeTestRule.onNodeWithText("예매 완료").performClick()

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        composeTestRule.onNodeWithText("지금 예매").assertIsDisplayed()
    }
}
