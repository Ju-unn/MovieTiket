package com.example.movietiket.movielist.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.common.fixture.harryPotterMovie
import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.movielist.model.MovieListRow
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 영화 목록 화면(MovieListScreen)의 정보 표시와 예매 버튼 클릭 동작을 검증한다
 */
@RunWith(AndroidJUnit4::class)
class MovieListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 영화 목록에 제목, 상영일, 러닝타임이 표시되는지 검증한다
    @Test
    fun `영화_목록의_제목_상영일_러닝타임을_표시한다`() {
        composeTestRule.setContent {
            MovieTiketTheme {
                MovieListScreen(
                    rows = MovieListRow.of(listOf(harryPotterMovie())),
                    theaterSelection = null,
                    onReserveClick = {},
                    onTheaterClick = {},
                    onTheaterSelectionDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("상영일: 2024.3.1 ~ 2024.3.28").assertIsDisplayed()
        composeTestRule.onNodeWithText("러닝타임: 152분").assertIsDisplayed()
    }

    // 지금 예매 버튼 클릭 시 해당 영화로 콜백이 호출되는지 검증한다
    @Test
    fun `지금_예매_버튼을_클릭하면_해당_영화로_콜백이_호출된다`() {
        val targetMovie = harryPotterMovie()
        var clickedMovie: Movie? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieListScreen(
                    rows = MovieListRow.of(listOf(targetMovie)),
                    theaterSelection = null,
                    onReserveClick = { clickedMovie = it },
                    onTheaterClick = {},
                    onTheaterSelectionDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("지금 예매").performClick()

        assertSame(targetMovie, clickedMovie)
    }
}
