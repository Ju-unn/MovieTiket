package com.example.movietiket.movielist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movietiket.common.model.movie.Movie
import com.example.movietiket.common.model.theater.Theater
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.movielist.model.MovieListRow
import com.example.movietiket.ui.theme.MovieTiketTheme

/**
 * 홈 탭의 영화 목록 (영화 3개마다 광고 배너가 끼어든다)
 * 상단바와 하단 네비게이션은 탭 공통 골격에서 그린다
 */
@Composable
fun MovieListScreen(
    rows: List<MovieListRow>,
    theaterSelection: List<Theater>?,
    onReserveClick: (Movie) -> Unit,
    onTheaterClick: (Theater) -> Unit,
    onTheaterSelectionDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(rows) { row ->
            when (row) {
                is MovieListRow.MovieRow ->
                    MovieListItem(movie = row.movie, onReserveClick = onReserveClick)
                MovieListRow.AdRow -> AdBanner()
            }
        }
    }

    // 극장 선택 중일 때만 바텀시트를 띄운다
    if (theaterSelection != null) {
        TheaterSelectionBottomSheet(
            theaters = theaterSelection,
            onTheaterClick = onTheaterClick,
            onDismissRequest = onTheaterSelectionDismiss,
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun MovieListScreenPreview() {
    MovieTiketTheme {
        MovieListScreen(
            rows = MovieListRow.of(MovieRepository.findAll().toList()),
            theaterSelection = null,
            onReserveClick = {},
            onTheaterClick = {},
            onTheaterSelectionDismiss = {},
        )
    }
}
