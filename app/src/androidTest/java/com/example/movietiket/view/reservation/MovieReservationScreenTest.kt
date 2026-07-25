package com.example.movietiket.view.reservation

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

@RunWith(AndroidJUnit4::class)
class MovieReservationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `영화_정보와_인원_수를_표시한다`() {
        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("상영일: 2024.3.1").assertIsDisplayed()
        composeTestRule.onNodeWithText("러닝타임: 152분").assertIsDisplayed()
        composeTestRule.onNodeWithText("소개").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun `인원_증가_감소_버튼을_클릭하면_각각의_콜백이_호출된다`() {
        var increased = false
        var decreased = false

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = { increased = true },
                    onDecreaseHeadCount = { decreased = true },
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("-").performClick()

        assertTrue(increased)
        assertTrue(decreased)
    }

    @Test
    fun `예매_완료_버튼을_클릭하면_확정_콜백이_호출된다`() {
        var confirmed = false

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onConfirmClick = { confirmed = true },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("예매 완료").performClick()

        assertTrue(confirmed)
    }

    @Test
    fun `뒤로_가기_버튼을_클릭하면_뒤로_가기_콜백이_호출된다`() {
        var backClicked = false

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onConfirmClick = {},
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        assertTrue(backClicked)
    }
}
