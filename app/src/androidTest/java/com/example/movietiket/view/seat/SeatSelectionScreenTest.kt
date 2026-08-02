package com.example.movietiket.view.seat

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.fixture.harryPotterReservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SeatSelectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `영화_제목과_초기_가격을_표시한다`() {
        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation(),
                    seatLimitExceededEvent = 0,
                    onSelectSeat = {},
                    onDeselectSeat = {},
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("0원").assertIsDisplayed()
    }

    @Test
    fun `미선택_좌석을_클릭하면_선택_콜백이_호출된다`() {
        var selectedSeat: String? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation(),
                    seatLimitExceededEvent = 0,
                    onSelectSeat = { selectedSeat = it },
                    onDeselectSeat = {},
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("A1").performClick()

        assertEquals("A1", selectedSeat)
    }

    @Test
    fun `선택된_좌석을_클릭하면_해제_콜백이_호출된다`() {
        var deselectedSeat: String? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation().selectSeat("A1"),
                    seatLimitExceededEvent = 0,
                    onSelectSeat = {},
                    onDeselectSeat = { deselectedSeat = it },
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("A1").performClick()

        assertEquals("A1", deselectedSeat)
    }

    @Test
    fun `좌석_선택이_완료되면_확인_다이얼로그를_거쳐_확정_콜백이_호출된다`() {
        var confirmed = false

        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation().selectSeat("A1"), // 인원 1명, 1석 선택 완료
                    seatLimitExceededEvent = 0,
                    onSelectSeat = {},
                    onDeselectSeat = {},
                    onConfirmClick = { confirmed = true },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("확인").performClick()
        composeTestRule.onNodeWithText("정말 예매하시겠습니까?").assertIsDisplayed()
        composeTestRule.onNodeWithText("예매 완료").performClick()

        assertTrue(confirmed)
    }

    @Test
    fun `좌석_미선택_상태에서는_확인_버튼을_눌러도_확정_콜백이_호출되지_않는다`() {
        var confirmed = false

        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation(), // 좌석 미선택
                    seatLimitExceededEvent = 0,
                    onSelectSeat = {},
                    onDeselectSeat = {},
                    onConfirmClick = { confirmed = true },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("확인").performClick()

        assertFalse(confirmed)
    }

    @Test
    fun `뒤로_가기_버튼을_클릭하면_뒤로_가기_콜백이_호출된다`() {
        var backClicked = false

        composeTestRule.setContent {
            MovieTiketTheme {
                SeatSelectionScreen(
                    reservation = harryPotterReservation(),
                    seatLimitExceededEvent = 0,
                    onSelectSeat = {},
                    onDeselectSeat = {},
                    onConfirmClick = {},
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        assertTrue(backClicked)
    }
}
