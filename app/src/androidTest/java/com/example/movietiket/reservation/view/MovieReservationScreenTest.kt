package com.example.movietiket.reservation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.common.fixture.harryPotterReservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

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
                    onSelectDate = {},
                    onSelectTime = {},
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("상영일: 2024.3.1 ~ 2024.3.28").assertIsDisplayed()
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
                    onSelectDate = {},
                    onSelectTime = {},
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
    fun `날짜_드롭다운에서_다른_날짜를_선택하면_콜백이_호출된다`() {
        var selectedDate: LocalDate? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onSelectDate = { selectedDate = it },
                    onSelectTime = {},
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("2024.3.1").performClick() // 날짜 Chip(기본 선택값)
        composeTestRule.onNodeWithText("2024.3.2").performClick() // 드롭다운 항목

        assertEquals(LocalDate.of(2024, 3, 2), selectedDate)
    }

    @Test
    fun `시간_드롭다운에서_다른_시간을_선택하면_콜백이_호출된다`() {
        var selectedTime: LocalTime? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onSelectDate = {},
                    onSelectTime = { selectedTime = it },
                    onConfirmClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("10:00").performClick() // 시간 Chip(기본 선택값, 평일)
        composeTestRule.onNodeWithText("14:00").performClick() // 드롭다운 항목

        assertEquals(LocalTime.of(14, 0), selectedTime)
    }

    @Test
    fun `좌석_선택_버튼을_클릭하면_확정_콜백이_호출된다`() {
        var confirmed = false

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieReservationScreen(
                    reservation = harryPotterReservation(),
                    onIncreaseHeadCount = {},
                    onDecreaseHeadCount = {},
                    onSelectDate = {},
                    onSelectTime = {},
                    onConfirmClick = { confirmed = true },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("좌석 선택").performClick()

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
                    onSelectDate = {},
                    onSelectTime = {},
                    onConfirmClick = {},
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("뒤로 가기").performClick()

        assertTrue(backClicked)
    }
}
