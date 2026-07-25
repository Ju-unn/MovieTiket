package com.example.movietiket.view.movielist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movietiket.fixture.harryPotterMovie
import com.example.movietiket.model.Movie
import com.example.movietiket.ui.theme.MovieTiketTheme
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `영화_목록의_제목_상영일_러닝타임을_표시한다`() {
        composeTestRule.setContent {
            MovieTiketTheme {
                MovieListScreen(movies = listOf(harryPotterMovie()), onReserveClick = {})
            }
        }

        composeTestRule.onNodeWithText("해리 포터와 마법사의 돌").assertIsDisplayed()
        composeTestRule.onNodeWithText("상영일: 2024.3.1").assertIsDisplayed()
        composeTestRule.onNodeWithText("러닝타임: 152분").assertIsDisplayed()
    }

    @Test
    fun `지금_예매_버튼을_클릭하면_해당_영화로_콜백이_호출된다`() {
        val targetMovie = harryPotterMovie()
        var clickedMovie: Movie? = null

        composeTestRule.setContent {
            MovieTiketTheme {
                MovieListScreen(
                    movies = listOf(targetMovie),
                    onReserveClick = { clickedMovie = it },
                )
            }
        }

        composeTestRule.onNodeWithText("지금 예매").performClick()

        assertSame(targetMovie, clickedMovie)
    }
}
