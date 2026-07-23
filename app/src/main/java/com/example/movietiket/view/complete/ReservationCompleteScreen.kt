package com.example.movietiket.view.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.model.MovieRepository
import com.example.movietiket.model.Reservation
import com.example.movietiket.ui.theme.MovieNoticeBackground
import com.example.movietiket.ui.theme.MovieTiketTheme

private val NOTICE_HEIGHT = 114.dp
private val NOTICE_TEXT_BOTTOM_PADDING = 10.dp
private val CONTENT_HORIZONTAL_PADDING = 24.dp

/**
 * 영화 예매 완료 화면 (취소 안내 / 예매 내역 / 결제 정보)
 */
@Composable
fun ReservationCompleteScreen(
    reservation: Reservation,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CompleteTopBar(onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CancellationNotice()
            ReservationSummary(reservation = reservation)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompleteTopBar(onBackClick: () -> Unit) {
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
private fun CancellationNotice() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(NOTICE_HEIGHT)
            .background(MovieNoticeBackground),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = stringResource(R.string.cancellation_notice),
            color = Color.White,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = NOTICE_TEXT_BOTTOM_PADDING),
        )
    }
}

@Composable
private fun ReservationSummary(reservation: Reservation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CONTENT_HORIZONTAL_PADDING, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = reservation.displayMovieTitle(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = reservation.displayScreeningDate(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = stringResource(R.string.head_count_result_format, reservation.displayHeadCount()),
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.payment_amount_format, reservation.displayTotalAmount()),
            fontSize = 20.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReservationCompleteScreenPreview() {
    MovieTiketTheme {
        ReservationCompleteScreen(
            reservation = Reservation.of(MovieRepository.findAll().toList().first()),
            onBackClick = {},
        )
    }
}
