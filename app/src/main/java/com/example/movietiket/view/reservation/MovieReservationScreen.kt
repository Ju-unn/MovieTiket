package com.example.movietiket.view.reservation

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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.movietiket.repository.MovieRepository
import com.example.movietiket.model.Reservation
import com.example.movietiket.ui.theme.MovieTiketTheme
import com.example.movietiket.view.component.BackNavigationTopBar
import com.example.movietiket.view.component.HeadCountSelector

private val POSTER_HEIGHT = 341.dp
private val CONTENT_HORIZONTAL_PADDING = 20.dp
private val CONFIRM_BUTTON_HEIGHT = 47.dp
private val CONFIRM_BUTTON_CORNER = 6.dp

// Figma 상영시간표 목업 (09:00 ~ 23:00, 2시간 간격)
private val SCREENING_TIMES = listOf(
    "09:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00",
)

// ScreeningDate 표시값(yyyy.M.d)을 날짜 선택 칩의 yyyy-MM-dd 형식으로 변환한다
private fun toHyphenatedDate(date: String): String {
    val (year, month, day) = date.split(".")
    return "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
}

/**
 * 영화 예매 화면 (포스터 / 영화 정보 / 인원 선택 / 예매 완료 버튼)
 */
@Composable
fun MovieReservationScreen(
    reservation: Reservation,
    onIncreaseHeadCount: () -> Unit,
    onDecreaseHeadCount: () -> Unit,
    onSelectTime: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { BackNavigationTopBar(onBackClick = onBackClick) },
        bottomBar = {
            ReservationBottomBar(
                screeningDate = reservation.displayScreeningDate(),
                selectedTime = reservation.displaySelectedTime(),
                headCount = reservation.displayHeadCount(),
                onIncrease = onIncreaseHeadCount,
                onDecrease = onDecreaseHeadCount,
                onSelectTime = onSelectTime,
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
            text = stringResource(R.string.screening_date_format, reservation.displayScreeningDate()),
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
    screeningDate: String,
    selectedTime: String,
    headCount: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onSelectTime: (String) -> Unit,
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
        ScreeningDateTimeSelector(
            screeningDate = screeningDate,
            selectedTime = selectedTime,
            onSelectTime = onSelectTime,
        )
        HeadCountSelector(
            headCount = headCount,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
        )
        SeatSelectionButton(onClick = onConfirmClick)
    }
}

@Composable
private fun ScreeningDateTimeSelector(
    screeningDate: String,
    selectedTime: String,
    onSelectTime: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DropdownChip(label = toHyphenatedDate(screeningDate), modifier = Modifier.weight(1f))
        ScreeningTimeSelector(
            selectedTime = selectedTime,
            onSelectTime = onSelectTime,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ScreeningTimeSelector(
    selectedTime: String,
    onSelectTime: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    // 아직 시간이 선택되지 않았다면 기본값(첫 시간)으로 초기화한다
    LaunchedEffect(Unit) {
        if (selectedTime.isEmpty()) onSelectTime(SCREENING_TIMES.first())
    }

    Column(modifier = modifier) {
        DropdownChip(
            label = selectedTime.ifEmpty { SCREENING_TIMES.first() },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SCREENING_TIMES.forEach { time ->
                DropdownMenuItem(
                    text = { Text(text = time) },
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
            reservation = Reservation.of(MovieRepository.findAll().toList().first()),
            onIncreaseHeadCount = {},
            onDecreaseHeadCount = {},
            onSelectTime = {},
            onConfirmClick = {},
            onBackClick = {},
        )
    }
}
