package com.example.movietiket.view.reservation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.model.MovieRepository
import com.example.movietiket.model.Reservation
import com.example.movietiket.ui.theme.MovieTiketTheme

private val POSTER_HEIGHT = 341.dp
private val CONTENT_HORIZONTAL_PADDING = 20.dp
private val CONFIRM_BUTTON_HEIGHT = 47.dp
private val CONFIRM_BUTTON_CORNER = 6.dp

/**
 * 영화 예매 화면 (포스터 / 영화 정보 / 인원 선택 / 예매 완료 버튼)
 */
@Composable
fun MovieReservationScreen(
    reservation: Reservation,
    onIncreaseHeadCount: () -> Unit,
    onDecreaseHeadCount: () -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ReservationTopBar(onBackClick = onBackClick) },
        bottomBar = {
            ReservationBottomBar(
                headCount = reservation.displayHeadCount(),
                onIncrease = onIncreaseHeadCount,
                onDecrease = onDecreaseHeadCount,
                onConfirmClick = onConfirmClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            ReservationPoster()
            ReservationMovieInformation(
                reservation = reservation,
                modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReservationTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_top_bar_title)) },
        navigationIcon = { BackNavigationIcon(onBackClick = onBackClick) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Composable
private fun BackNavigationIcon(onBackClick: () -> Unit) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.navigate_back_description),
        )
    }
}

@Composable
private fun ReservationPoster() {
    Image(
        painter = painterResource(R.drawable.movieposter),
        contentDescription = stringResource(R.string.movie_poster_description),
        modifier = Modifier
            .fillMaxWidth()
            .height(POSTER_HEIGHT),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ReservationMovieInformation(reservation: Reservation, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        Text(
            text = reservation.displayMovieTitle(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.screening_date_format, reservation.displayScreeningDate()),
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(R.string.running_time_format, reservation.displayRunningTime()),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = reservation.displaySynopsis(),
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun ReservationBottomBar(
    headCount: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = CONTENT_HORIZONTAL_PADDING, end = CONTENT_HORIZONTAL_PADDING, top = 9.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeadCountSelector(
            headCount = headCount,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
        )
        ReservationConfirmButton(onClick = onConfirmClick)
    }
}

@Composable
private fun ReservationConfirmButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(CONFIRM_BUTTON_HEIGHT),
        shape = RoundedCornerShape(CONFIRM_BUTTON_CORNER),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = stringResource(R.string.reservation_confirm),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieReservationScreenPreview() {
    MovieTiketTheme {
        MovieReservationScreen(
            reservation = Reservation.of(MovieRepository.findAll().toList().first()),
            onIncreaseHeadCount = {},
            onDecreaseHeadCount = {},
            onConfirmClick = {},
            onBackClick = {},
        )
    }
}
