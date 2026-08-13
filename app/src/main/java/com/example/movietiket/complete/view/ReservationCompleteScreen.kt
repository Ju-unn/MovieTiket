package com.example.movietiket.complete.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.ui.theme.MovieNoticeBackground
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.common.view.BackNavigationTopBar

// 디자인 "3단계 - 예매 상세" 기준 수치
private val NOTICE_HEIGHT = 116.dp
private val NOTICE_TEXT_BOTTOM_PADDING = 10.dp
private val CONTENT_HORIZONTAL_PADDING = 24.dp
private val TITLE_TOP_PADDING = 33.dp
private val SCHEDULE_TOP_PADDING = 20.dp
private val SEATS_TOP_PADDING = 78.dp
private val PRICE_TOP_PADDING = 33.dp

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
        topBar = { BackNavigationTopBar(onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CancellationNotice()
            ReservationSummary(reservation = reservation)
        }
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
            .padding(horizontal = CONTENT_HORIZONTAL_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(TITLE_TOP_PADDING))
        Text(
            text = reservation.displayMovieTitle(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(SCHEDULE_TOP_PADDING))
        Text(
            text = stringResource(
                R.string.screening_datetime_format,
                reservation.displaySelectedDate(),
                reservation.displaySelectedTime(),
            ),
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(SEATS_TOP_PADDING))
        Text(
            text = stringResource(
                R.string.head_count_seats_theater_format,
                stringResource(R.string.head_count_result_format, reservation.displayHeadCount()),
                reservation.displaySelectedSeats(),
                reservation.displayTheaterName(),
            ),
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            // 극장 이름이 한 줄을 넘기면 뒤를 "..."으로 줄인다
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(PRICE_TOP_PADDING))
        Text(
            text = stringResource(R.string.payment_amount_format, reservation.totalAmountWon()),
            fontSize = 19.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReservationCompleteScreenPreview() {
    MovieTiketTheme {
        ReservationCompleteScreen(
            reservation = Reservation.of(MovieRepository.findAll().toList().first(), TheaterRepository.findAll().toList().first()),
            onBackClick = {},
        )
    }
}
