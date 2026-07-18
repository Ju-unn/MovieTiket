package com.example.movietiket.view.movielist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.model.Movie
import com.example.movietiket.model.MovieRepository
import com.example.movietiket.ui.theme.MovieTiketTheme

/**
 * 영화 목록의 한 항목 (포스터 / 영화 정보 / 지금 예매 버튼)
 */
@Composable
fun MovieListItem(
    movie: Movie,
    onReserveClick: (Movie) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MoviePosterThumbnail()
        Spacer(modifier = Modifier.width(16.dp))
        MovieSummary(movie = movie, modifier = Modifier.weight(1f))
        ReserveNowButton(onClick = { onReserveClick(movie) })
    }
}

@Composable
private fun MoviePosterThumbnail() {
    Image(
        painter = painterResource(R.drawable.movieposter),
        contentDescription = stringResource(R.string.movie_poster_description),
        modifier = Modifier.size(width = 72.dp, height = 96.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun MovieSummary(movie: Movie, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = movie.displayTitle(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.screening_date_format, movie.displayScreeningDate()),
            fontSize = 13.sp,
        )
        Text(
            text = stringResource(R.string.running_time_format, movie.displayRunningTime()),
            fontSize = 13.sp,
        )
    }
}

@Composable
private fun ReserveNowButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(R.string.reserve_now))
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListItemPreview() {
    MovieTiketTheme {
        MovieListItem(
            movie = MovieRepository.findAll().toList().first(),
            onReserveClick = { movie -> },
        )
    }
}
