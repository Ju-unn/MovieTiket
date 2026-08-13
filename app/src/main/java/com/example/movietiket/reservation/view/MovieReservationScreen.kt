package com.example.movietiket.reservation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.movietiket.common.repository.MovieRepository
import com.example.movietiket.common.repository.TheaterRepository
import com.example.movietiket.common.model.DisplayDateFormat
import com.example.movietiket.common.model.Reservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.common.view.BackNavigationTopBar
import com.example.movietiket.reservation.view.HeadCountSelector
import java.time.LocalDate
import java.time.LocalTime

private val POSTER_HEIGHT = 341.dp
private val CONTENT_HORIZONTAL_PADDING = 20.dp
private val CONFIRM_BUTTON_HEIGHT = 47.dp
private val CONFIRM_BUTTON_CORNER = 6.dp

/**
 * 영화 예매 화면 (포스터 / 영화 정보 / 날짜·시간·인원 선택 / 예매 완료 버튼)
 */
@Composable
fun MovieReservationScreen(
    reservation: Reservation,
    onIncreaseHeadCount: () -> Unit,
    onDecreaseHeadCount: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onSelectTime: (LocalTime) -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BackNavigationTopBar(onBackClick = onBackClick) },
        bottomBar = {
            ReservationBottomBar(
                availableDates = reservation.availableDates(),
                selectedDate = reservation.displaySelectedDate(),
                onSelectDate = onSelectDate,
                availableTimes = reservation.availableTimes(),
                selectedTime = reservation.displaySelectedTime(),
                onSelectTime = onSelectTime,
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
            text = stringResource(R.string.screening_date_format, reservation.displayScreeningPeriod()),
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(R.string.running_time_format, reservation.runningTimeMinutes()),
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
    availableDates: List<LocalDate>,
    selectedDate: String,
    onSelectDate: (LocalDate) -> Unit,
    availableTimes: List<LocalTime>,
    selectedTime: String,
    onSelectTime: (LocalTime) -> Unit,
    headCount: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(start = CONTENT_HORIZONTAL_PADDING, end = CONTENT_HORIZONTAL_PADDING, top = 9.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ScreeningDateSelector(
                availableDates = availableDates,
                selectedDate = selectedDate,
                onSelectDate = onSelectDate,
                modifier = Modifier.weight(1f),
            )
            ScreeningTimeSelector(
                availableTimes = availableTimes,
                selectedTime = selectedTime,
                onSelectTime = onSelectTime,
                modifier = Modifier.weight(1f),
            )
        }
        HeadCountSelector(
            headCount = headCount,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
        )
        SeatSelectionButton(onClick = onConfirmClick)
    }
}

@Composable
private fun ScreeningDateSelector(
    availableDates: List<LocalDate>,
    selectedDate: String,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        DropdownChip(
            label = selectedDate,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableDates.forEach { date ->
                DropdownMenuItem(
                    text = { Text(text = DisplayDateFormat.format(date)) },
                    onClick = {
                        onSelectDate(date)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ScreeningTimeSelector(
    availableTimes: List<LocalTime>,
    selectedTime: String,
    onSelectTime: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        DropdownChip(
            label = selectedTime,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableTimes.forEach { time ->
                DropdownMenuItem(
                    text = { Text(text = DisplayDateFormat.format(time)) },
                    onClick = {
                        onSelectTime(time)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun DropdownChip(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, fontSize = 16.sp)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = stringResource(R.string.screening_time_description),
        )
    }
}

@Composable
private fun SeatSelectionButton(onClick: () -> Unit) {
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
            text = stringResource(R.string.select_seat),
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
            reservation = Reservation.of(MovieRepository.findAll().toList().first(), TheaterRepository.findAll().toList().first()),
            onIncreaseHeadCount = {},
            onDecreaseHeadCount = {},
            onSelectDate = {},
            onSelectTime = {},
            onConfirmClick = {},
            onBackClick = {},
        )
    }
}
