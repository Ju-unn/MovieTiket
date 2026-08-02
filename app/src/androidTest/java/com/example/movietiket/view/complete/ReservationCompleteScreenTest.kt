package com.example.movietiket.view.complete

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.fixture.harryPotterReservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class ReservationCompleteScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `예매_내역과_결제_금액을_표시한다`() {
        // A1은 B등급 좌석(10,000원)이다
        val reservation = harryPotterReservation().selectTime(LocalTime.of(18, 0)).selectSeat("A1")

        composeTestRule.setContent {
            MovieTiketTheme {
                ReservationCompleteScreen(reservation = reservation, onBackClick = {})
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-03-01 18:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("일반 1명 | A1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10,000원 (현장 결제)").assertIsDisplayed()
    }

    @Test
    fun `뒤로_가기_버튼을_클릭하면_뒤로_가기_콜백이_호출된다`() {
        var backClicked = false

        composeTestRule.setContent {
            MovieTiketTheme {
                ReservationCompleteScreen(
                    reservation = harryPotterReservation(),
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        assertTrue(backClicked)
    }
}
