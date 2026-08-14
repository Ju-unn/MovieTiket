package com.example.movietiket.movielist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.ui.theme.MovieTiketTheme

private val ITEM_HEIGHT = 132.dp
private val ITEM_CONTENT_PADDING = 12.dp
private val POSTER_SIZE = DpSize(76.dp, 108.dp)
private val POSTER_TO_TEXT_GAP = 15.dp
private val RESERVE_BUTTON_SIZE = DpSize(88.dp, 36.dp)
private val RESERVE_BUTTON_CORNER = 6.dp

/**
 * 영화 목록의 한 항목 (포스터 / 영화 정보 / 지금 예매 버튼)
 */
@Composable
fun MovieListItem(
    movie: Movie,
    onReserveClick: (Movie) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ITEM_HEIGHT)
            .background(Color.White),
    ) {
        Row(modifier = Modifier.padding(ITEM_CONTENT_PADDING)) {
            MoviePosterThumbnail(posterRes = posterResFor(movie.id()))
            Spacer(modifier = Modifier.width(POSTER_TO_TEXT_GAP))
            MovieSummary(movie = movie)
        }
        ReserveNowButton(
            onClick = { onReserveClick(movie) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = ITEM_CONTENT_PADDING, bottom = ITEM_CONTENT_PADDING),
        )
    }
}

@Composable
private fun MoviePosterThumbnail(posterRes: Int) {
    Image(
        painter = painterResource(posterRes),
        contentDescription = stringResource(R.string.movie_poster_description),
        modifier = Modifier.size(width = POSTER_SIZE.width, height = POSTER_SIZE.height),
        contentScale = ContentScale.Crop,
    )
}

// id5~9는 3편(비밀의 방/아즈카반의 죄수/불의 잔)이 광고 배너 테스트를 위해 반복된 것이라 id % 5로 매핑한다
internal fun posterResFor(movieId: Int): Int = when (movieId % 5) {
    1 -> R.drawable.poster_1
    2 -> R.drawable.poster_2
    3 -> R.drawable.poster_3
    else -> R.drawable.movieposter
}

@Composable
private fun MovieSummary(movie: Movie) {
    Column {
        Text(
            text = movie.displayTitle(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.screening_date_format, movie.displayScreeningPeriod()),
            fontSize = 12.sp,
        )
        Text(
            text = stringResource(R.string.running_time_format, movie.runningTimeMinutes()),
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun ReserveNowButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.size(width = RESERVE_BUTTON_SIZE.width, height = RESERVE_BUTTON_SIZE.height),
        shape = RoundedCornerShape(RESERVE_BUTTON_CORNER),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.reserve_now),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListItemPreview() {
    MovieTiketTheme {
        MovieListItem(
            movie = MovieRepository.findAll().toList().first(),
            onReserveClick = {},
        )
    }
}
