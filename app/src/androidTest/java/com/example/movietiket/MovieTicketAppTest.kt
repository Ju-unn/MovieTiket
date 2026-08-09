package com.example.movietiket

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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

    // 영화 목록 → 예매 화면 → 좌석 선택(A1) → 예매 확정 다이얼로그 → 예매 완료까지 진행한다
    private fun ComposeContentTestRule.navigateToReservationComplete() {
        onAllNodesWithText("지금 예매").onFirst().performClick()
        onNodeWithText("좌석 선택").performClick()
        onNodeWithText("A1").performClick()
        onNodeWithText("확인").performClick()
        onNodeWithText("예매 완료").performClick()
    }

    @Test
    fun `목록에서_지금_예매를_클릭하면_예매_화면으로_이동한다`() {
        composeTestRule.setContent { MovieTicketApp() }

        composeTestRule.onAllNodesWithText("지금 예매").onFirst().performClick()

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("좌석 선택").assertIsDisplayed()
    }

    @Test
    fun `좌석_선택_후_예매를_확정하면_완료_화면으로_이동한다`() {
        composeTestRule.setContent { MovieTicketApp() }

        composeTestRule.navigateToReservationComplete()

        composeTestRule.onNodeWithText("일반 1명 | A1").assertIsDisplayed()
    }

    @Test
    fun `예매_화면에서_뒤로_가기를_클릭하면_목록_화면으로_돌아간다`() {
        composeTestRule.setContent { MovieTicketApp() }
        composeTestRule.onAllNodesWithText("지금 예매").onFirst().performClick()

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        composeTestRule.onAllNodesWithText("지금 예매").onFirst().assertIsDisplayed()
    }

    @Test
    fun `완료_화면에서_뒤로_가기를_클릭하면_목록_화면으로_돌아간다`() {
        composeTestRule.setContent { MovieTicketApp() }
        composeTestRule.navigateToReservationComplete()

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        composeTestRule.onAllNodesWithText("지금 예매").onFirst().assertIsDisplayed()
    }
}
