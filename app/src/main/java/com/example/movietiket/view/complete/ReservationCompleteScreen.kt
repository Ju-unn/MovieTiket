package com.example.movietiket.view.complete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movietiket.R
import com.example.movietiket.model.MovieRepository
import com.example.movietiket.model.Reservation
import com.example.movietiket.ui.theme.MovieTiketTheme

/**
 * 영화 예매 완료 화면 (예매 내역 / 결제 정보)
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ReservationSummary(reservation = reservation)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompleteTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.reservation_complete_title)) },
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
private fun ReservationSummary(reservation: Reservation) {
    Text(
        text = reservation.displayMovieTitle(),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = reservation.displayScreeningDate(),
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = stringResource(R.string.head_count_result_format, reservation.displayHeadCount()),
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.payment_amount_format, reservation.displayTotalAmount()),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
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
