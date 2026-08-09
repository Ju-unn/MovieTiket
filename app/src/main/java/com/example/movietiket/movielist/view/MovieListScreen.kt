package com.example.movietiket.movielist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.movietiket.R
import com.example.movietiket.common.model.Movie
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.movielist.view.MovieListItem

/**
 * 영화 목록 화면
 */
@Composable
fun MovieListScreen(
    movies: List<Movie>,
    onReserveClick: (Movie) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MovieListTopBar() },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(movies) { movie ->
                MovieListItem(movie = movie, onReserveClick = onReserveClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListTopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_top_bar_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun MovieListScreenPreview() {
    MovieTiketTheme {
        MovieListScreen(
            movies = MovieRepository.findAll().toList(),
            onReserveClick = {},
        )
    }
}
